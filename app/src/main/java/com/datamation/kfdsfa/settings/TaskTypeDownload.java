package com.datamation.kfdsfa.settings;


public enum TaskTypeDownload {

    Controllist(1),
    Items(2),
    ItemLoc(3),
    Type(4),
    Bank(5),
    Route(6),
    RouteDet(7),
    Freeslab(8),
    Freemslab(9),
    Freehed(10),
    Freedet(11),
    Freedeb(12),
    Freeitem(13),
    fddbnote(14),
    PushMsgHedDet(15),
    CusPRecHed(16),
    CusPRecDet(17),
    Reason(18),
    ItemPri(19),
    Area(20),
    Locations(21),
    Towns(22),
    Groups(23),
    Brand(24),
    Payments(25),
    OrderStatus(26),
    RepDetails(27);


    int value;

    private TaskTypeDownload(int value) {
        this.value = value;
    }

}
