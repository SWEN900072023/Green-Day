package edu.unimelb.swen90007.mes.model;

public class SectionTickets {
    public int sectionId;
    public int remainingTickets;

    public SectionTickets(int number){
        this.remainingTickets = number;
    }
    public SectionTickets(int sectionId, int number){
        this.sectionId = sectionId;
        this.remainingTickets = number;
    }
}
