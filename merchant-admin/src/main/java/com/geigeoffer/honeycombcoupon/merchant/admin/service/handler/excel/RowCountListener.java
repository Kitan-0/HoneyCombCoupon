package com.geigeoffer.honeycombcoupon.merchant.admin.service.handler.excel;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.event.AnalysisEventListener;
import lombok.Getter;

public class RowCountListener extends AnalysisEventListener<Object> {
    @Getter
    private int rowcount = 0;
    @Override
    public void invoke(Object data, AnalysisContext context) {
        rowcount++;
    }
    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {

    }
}
