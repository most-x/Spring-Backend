package kr.co.mostx.japi.service;

import kr.co.mostx.japi.dto.AssetDashboardDTO;
import kr.co.mostx.japi.repository.AssetDashboardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AssetDashboardService {
    private final AssetDashboardRepository assetDashboardRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public List<AssetDashboardDTO> findDashboard() {
        List<AssetDashboardDTO> assetDashboardDTOS = assetDashboardRepository.findDashboard();

        return assetDashboardDTOS
                .stream()
                .map(assetDashboardDTO -> modelMapper.map(assetDashboardDTO, AssetDashboardDTO.class))
                .toList();
    }
}
