package com.aurum.trader.ai;

import java.util.LinkedHashMap;
import java.util.Map;

public final class AIEngine {
    public static Map<String, Object> runAnalysis(String symbol, double trend, double support, double resistance, double volume, double risk) {
        Map<String, Object> result = new LinkedHashMap<>();
        String smc = SmartMoneyConceptsAnalyzer.analyze(trend, support, resistance);
        String ema = EMATrendAnalyzer.analyze(trend + 0.1, -0.1);
        boolean bos = BOSDetector.detect(trend + 0.2, support);
        boolean choch = CHOCHDetector.detect(resistance, volume);
        boolean fvg = FairValueGapDetector.detect(volume);
        boolean liquidity = LiquiditySweepDetector.detect(risk + 0.1);
        double atr = ATRAnalyzer.estimateRange(0.01 + risk * 0.005, 1.0);
        String mtf = MultiTimeframeAnalyzer.analyze(trend, support, resistance);
        double positionSize = RiskManager.calculatePositionSize(10000.0, risk, atr);
        int confidence = TradeConfidenceScore.calculate(trend + 0.6, support + 0.2, resistance + 0.1, volume + 0.3, risk);
        result.put("symbol", symbol);
        result.put("smc", smc);
        result.put("ema", ema);
        result.put("bos", bos);
        result.put("choch", choch);
        result.put("fvg", fvg);
        result.put("liquidity", liquidity);
        result.put("mtf", mtf);
        result.put("positionSize", positionSize);
        result.put("confidence", confidence);
        result.put("decision", TradeConfidenceScore.label(confidence));
        return result;
    }
}
