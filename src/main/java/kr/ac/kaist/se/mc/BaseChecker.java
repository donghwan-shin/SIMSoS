package kr.ac.kaist.se.mc;

import kr.ac.kaist.se.simulator.SIMResult;

/**
 * BaseChecker.java

 * Author: Junho Kim <jhim@se.kaist.ac.kr>

 * The MIT License (MIT)

 * Copyright (c) 2016 Junho Kim

 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: TBD
 */

public class BaseChecker implements CheckerInterface{

    /*
     * BLTL Model Checker for SIMSoS
     * 1. Getting a sample sequence from the simulator
     * 2. Check whether the sample sequence satisfies or not the condition (temporal logic)
     * 3. Return Bernoulli random variable value (0, not satisfied or 1, satisfied)
     */

    public enum comparisonType {LESS_THAN, GREATER_THAN, EQUAL_TO, LESS_THAN_AND_EQUAL_TO, GREATER_THAN_AND_EQUAL_TO}

    private int baseTick;
    private int baseSoSBenefit;
    private comparisonType type;
    private int minTick;
    private int maxTick;
    private boolean negation = false;

    /**
     * Constructor for BLTL model Checker
     * @param baseTick baseline of the time tick, BLTL Checker will evaluate the sample sequence based on this tick
     * @param baseSoSBenefit baseline of SoS benefit, BLTL Checker will evaluate the sample sequence based on this SoS benefit
     */
    public BaseChecker(int baseTick, int baseSoSBenefit, comparisonType type){
        this.baseTick = baseTick;
        this.baseSoSBenefit = baseSoSBenefit;
        this.type = type;
        this.minTick = Integer.MAX_VALUE;
        this.maxTick = Integer.MIN_VALUE;
    }

    /**
     * Evaluate the sample sequence based on the base tick and base SoS benefit
     * @param res Simulation result (sample sequence)
     * @return 1 - satisfied, 0 - not satisfied
     */
    public int evaluateSample(SIMResult res){
        int sampleTick = res.getNumTicks();
        int sampleBenefit = res.getSoSBenefit();
        if(sampleTick <= baseTick){
            switch(this.type){
                case LESS_THAN:
                    if(sampleBenefit < this.baseSoSBenefit)
                        return 1;
                    break;
                case GREATER_THAN:
                    if(sampleBenefit > this.baseSoSBenefit)
                        return 1;
                    break;
                case EQUAL_TO:
                    if(sampleBenefit == this.baseSoSBenefit)
                        return 1;
                    break;
                case LESS_THAN_AND_EQUAL_TO:
                    if(sampleBenefit <= this.baseSoSBenefit)
                        return 1;
                    break;
                case GREATER_THAN_AND_EQUAL_TO:
                    if(sampleBenefit >= this.baseSoSBenefit)
                        return 1;
                    break;
            }
            if(this.minTick >= res.getNumTicks())
                this.minTick = res.getNumTicks();
            else if(this.maxTick <= res.getNumTicks())
                this.maxTick = res.getNumTicks();
        }
        return 0;
    }

    public void setNegation(){
        this.negation = true;
    }

    public void setNotNegation(){
        this.negation = false;
    }

    public int getMinTick(){
        return this.minTick;
    }

    public int getMaxTick(){
        return this.maxTick;
    }
}
