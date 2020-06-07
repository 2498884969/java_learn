package com.qxh.autoconfig.service;

import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

@Profile("Java7")
@Service
public class Java7CalculateService implements CalculateService {
    @Override
    public Integer sum(Integer... values) {
        System.out.println("java7...........");
        int sum = 0;
        for (int i = 0; i < values.length; i++) {
            sum += values[i];
        }
        return sum;
    }

    public static void main(String[] args) {
        System.out.println(
                new Java7CalculateService().sum(
                        1,2,3,4,5,6,7,8,9,10
                )
        );
    }
}
