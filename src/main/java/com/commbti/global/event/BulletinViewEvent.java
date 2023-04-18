package com.commbti.global.event;

import com.commbti.domain.bulletinboard.entity.Bulletin;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class BulletinViewEvent {

    private Bulletin bulletin;
}
