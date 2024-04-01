package com.mostx.asset.service;

import com.mostx.asset.dto.DashboardDTO;
import com.mostx.asset.repository.DashboardRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DashboardService {
    private final DashboardRepository dashboardRepository;
    private final ModelMapper modelMapper = new ModelMapper();

    public List<DashboardDTO> findDashboard() {
        List<DashboardDTO> dashboardDTOS = dashboardRepository.findDashboard();

        return dashboardDTOS
                .stream()
                .map(dashboardDTO -> modelMapper.map(dashboardDTO, DashboardDTO.class))
                .toList();
    }
}
