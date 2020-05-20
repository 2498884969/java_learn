package com.qxh.sb22.pro;

import org.springframework.stereotype.Component;

@Component
public class Flag {

    private boolean canOperate = true;

    public boolean isCanOperate() {
        return canOperate;
    }

    public void setCanOperate(boolean canOperate) {
        this.canOperate = canOperate;
    }
}
