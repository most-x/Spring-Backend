package com.mostx.asset.service;

import com.mostx.asset.entity.Asset;
import com.mostx.asset.entity.AssetDepreciation;
import com.mostx.asset.repository.ScheduleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SchedulerService {
    private final ScheduleRepository scheduleRepository;
    private final AssetService assetService;
    private final AssetDepreciationService assetDepreciationService;

    @Scheduled(cron = "0 0 16 13 * *")
    public void run() {
        List<Asset> assets = scheduleRepository.findAll();
        AssetDepreciation assetDepreciation;
        int depreciationCost, accumlatedDepreciation, bookValue;

        for (Asset asset : assets) {
            if (asset.getInitialStartDate() != null) {
                if (asset.getUsefulLife() != null) {
                    depreciationCost = asset.getTotalPrice() / (asset.getUsefulLife() * 12);
                } else {
                    System.out.println("내용연수가 입력되지 않은 자산입니다. 자산이름 : " + asset.getProductName() + ", 자산번호 : " + asset.getWrmsAssetCode());
                    continue;
                }
                assetDepreciation = scheduleRepository.findDepreciation(asset.getSno());
                accumlatedDepreciation = assetDepreciation.getAccumlatedDepreciation() + depreciationCost;
                bookValue = assetDepreciation.getBookValue() - depreciationCost;

                if(bookValue < 0 || assetDepreciation.getBookValue() == asset.getTotalPrice()){
                    System.out.println("해당 자산에는 더이상 감가상각을 진행할 수 없습니다. 자산번호 : " + asset.getWrmsAssetCode());
                    continue;
                }

                AssetDepreciation assetDepreciation1 = new AssetDepreciation();
                assetDepreciation1.setAssetSno(asset);
                assetDepreciation1.setDepreciationCost(depreciationCost);
                assetDepreciation1.setAccumlatedDepreciation(accumlatedDepreciation);
                assetDepreciation1.setBookValue(bookValue);
                assetDepreciation1.setDepreciationDate(LocalDate.now());

                assetDepreciationService.saveDepreciation(assetDepreciation1);

                assetService.update(asset.getSno(), depreciationCost, accumlatedDepreciation, bookValue);
            }
        }
    }
}
