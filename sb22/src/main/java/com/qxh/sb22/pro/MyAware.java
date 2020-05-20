package com.qxh.sb22.pro;

import org.springframework.beans.factory.Aware;

public interface MyAware extends Aware {

    void setFlag(Flag flag);

}
