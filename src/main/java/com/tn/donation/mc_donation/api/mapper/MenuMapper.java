package com.tn.donation.mc_donation.api.mapper;

import com.tn.donation.mc_donation.api.dto.MenuDTO;
import com.tn.donation.mc_donation.infrastructure.repository.jpa.entity.MenuEntity;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class MenuMapper {

    public static MenuDTO toResponse(MenuEntity menuEntity) {
        return  new MenuDTO(
                menuEntity.getId(),
                menuEntity.getName(),
                menuEntity.getPath(),
                menuEntity.getIcon(),
                menuEntity.getOrderIndex()
        );
    }
}
