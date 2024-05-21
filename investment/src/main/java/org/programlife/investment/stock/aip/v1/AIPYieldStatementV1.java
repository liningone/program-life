package org.programlife.investment.stock.aip.v1;

import org.programlife.investment.stock.aip.AIPYieldStatement;
import org.programlife.investment.stock.calculation.YieldData;

import java.util.List;

public class AIPYieldStatementV1 implements AIPYieldStatement {
    public List<YieldData> yieldForEachInvestment;

    public YieldData currentYield;
}
