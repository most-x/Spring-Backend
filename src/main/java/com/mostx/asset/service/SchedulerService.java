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

    @Scheduled(cron = "0 1 * * * *")
    public void run() {
        List<Asset> assets = scheduleRepository.findAll();
        List<AssetDepreciation> assetDepreciation;
        int depreciationCost, accumlatedDepreciation, bookValue, usefulListMonth;
        LocalDate nowDate = LocalDate.now();

        for (Asset asset : assets) {
            if (asset.getInitialStartDate() != null && asset.getAssetStatus().equals("정상")) {
                if (asset.getUsefulLife() != null) {
                    usefulListMonth = asset.getUsefulLife() * 12;
                    depreciationCost = asset.getTotalPrice() / usefulListMonth;
                } else {
                    System.out.println("내용연수가 입력되지 않은 자산입니다. 자산이름 : " + asset.getProductName() + ", 자산번호 : " + asset.getWrmsAssetCode());
                    continue;
                }
                assetDepreciation = scheduleRepository.findDepreciation(asset.getSno());

                if (assetDepreciation.size() == usefulListMonth - 1) {
                    depreciationCost = assetDepreciation.get(0).getBookValue();
                }

                // if : 감가상각진행현황이 없을 경우 신규 등록
                // else : 감가상각진행현황이 있을 경우 추가 등록
                if (assetDepreciation.isEmpty()) {
                    if (nowDate.isEqual(asset.getInitialStartDate().plusDays(30))) {
                        accumlatedDepreciation = depreciationCost;
                        bookValue = asset.getSupplyPrice() - depreciationCost;
                    } else if (nowDate.isAfter(asset.getInitialStartDate().plusDays(30))) {
                        accumlatedDepreciation = depreciationCost;
                        bookValue = asset.getSupplyPrice() - depreciationCost;
                        nowDate = asset.getInitialStartDate().plusDays(30);
                    } else {
                        continue;
                    }
                } else {
                    if (nowDate.isEqual(assetDepreciation.get(0).getDepreciationDate().plusDays(30))) {
                        // 자산의 장부가액이 0원일 경우 감가진행 없음
                        if(assetDepreciation.get(0).getBookValue() == 0){
                            continue;
                        }

                        accumlatedDepreciation = assetDepreciation.get(0).getAccumlatedDepreciation() + depreciationCost;
                        bookValue = assetDepreciation.get(0).getBookValue() - depreciationCost;
                    } else if (nowDate.isAfter(assetDepreciation.get(0).getDepreciationDate().plusDays(30))) {
                        // 자산의 장부가액이 0원일 경우 감가진행 없음
                        if(assetDepreciation.get(0).getBookValue() == 0){
                            continue;
                        }

                        accumlatedDepreciation = assetDepreciation.get(0).getAccumlatedDepreciation() + depreciationCost;
                        bookValue = assetDepreciation.get(0).getBookValue() - depreciationCost;
                        nowDate = assetDepreciation.get(0).getDepreciationDate().plusDays(30);
                    } else {
                        continue;
                    }
                }

                AssetDepreciation assetDepreciation1 = new AssetDepreciation();
                assetDepreciation1.setAssetSno(asset);
                assetDepreciation1.setDepreciationCost(depreciationCost);
                assetDepreciation1.setAccumlatedDepreciation(accumlatedDepreciation);
                assetDepreciation1.setBookValue(bookValue);
                assetDepreciation1.setDepreciationDate(LocalDate.now());

                assetDepreciationService.saveDepreciation(assetDepreciation1);

                assetService.updateDepreciation(asset.getSno(), depreciationCost, accumlatedDepreciation, bookValue);
            }
        }
    }
}
