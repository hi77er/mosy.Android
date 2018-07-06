package com.mosy.kalin.mosy.Helpers;

import com.mosy.kalin.mosy.R;

public class DrawablesPathHelper {

    public static int getDrawableIdByFilterId(final String filterId) {
        int drawableId = 0;

        switch (filterId.toUpperCase()) {
            case "E48D0871-500E-4D41-8620-840308901970":
                drawableId = R.drawable.filter_fgwhite_bgmain_wifiavailable;
                break;
            case "93252A33-D2A7-44A6-B375-0496EB3B5F9E":
                drawableId = R.drawable.filter_fgwhite_bgmain_workingfriendly;
                break;
            case "602F1863-A209-4E34-BC5E-871AE52AE684":
                drawableId = R.drawable.filter_fgwhite_bgmain_bikeaccessible;
                break;
            case "4F952337-5F15-4EFA-934A-7A948800B93F":
                drawableId = R.drawable.filter_fgwhite_bgmain_babyfriendly;
                break;
            case "BE06BC04-CB07-4884-866B-907132DE2944":
                drawableId = R.drawable.filter_fgwhite_bgmain_funplace;
                break;
            case "245733F1-35C4-4497-B188-59B1A69480AA":
                drawableId = R.drawable.filter_fgwhite_bgmain_parkingavailable;
                break;
            case "5CF4A8FA-CA93-4D36-BD67-E4E6A26D751E":
                drawableId = R.drawable.filter_fgwhite_bgmain_petfriendly;
                break;
            case "D7C4C6BB-A717-4774-B3B3-E4C23893D2BF":
                drawableId = R.drawable.filter_fgwhite_bgmain_romanticplace;
                break;
            case "C8C0E9CA-4F73-4A01-93DB-00573BD2E7F0":
                drawableId = R.drawable.filter_fgwhite_bgmain_wheelchairfriendly;
                break;
            case "39814B44-BC9A-4172-B91A-D190213DB112":
                drawableId = R.drawable.filter_fgwhite_bgmain_nosmoking;
                break;
            case "EA50942C-B218-430D-AF44-233FC64041EA":
                drawableId = R.drawable.flag_bulgaria;
                break;
            case "E1587CC6-ECAD-4884-AD7B-43148FAA68A1":
                drawableId = R.drawable.flag_russia;
                break;
            case "D33905F5-127A-41C1-9621-54AD3639676E":
                drawableId = R.drawable.flag_greece;
                break;
            case "ECF4571E-18CC-4F36-928F-6858CC89D4F7":
                drawableId = R.drawable.flag_spain;
                break;
            case "2E2B3A2D-F132-49A5-9AA6-6FD72A186836":
                drawableId = R.drawable.flag_portugal;
                break;
            case "3E585B93-50E2-4F48-8CD5-81AA7680684A":
                drawableId = R.drawable.flag_germany;
                break;
            case "F5C646F2-68F7-4147-A09B-854DDD3C1800":
                drawableId = R.drawable.flag_cuba;
                break;
            case "4FCEE53E-05C1-40A5-9DA5-86BC99EB6E5A":
                drawableId = R.drawable.flag_angola;
                break;
            case "E9565EFA-EA0C-4E61-88E6-9464B6B7CF64":
                drawableId = R.drawable.flag_turkey;
                break;
            case "737D1745-CD04-4741-9E91-B91FA1185662":
                drawableId = R.drawable.flag_brazil;
                break;
            case "C1958A8B-827D-4E5B-BFF3-E718B3F3A8BB":
                drawableId = R.drawable.flag_italy;
                break;
            case "7029562A-811D-4446-BB16-E9723BCA89DA":
                drawableId = R.drawable.flag_colombia;
                break;
            case "AA3B60B2-4FAE-4037-BED4-F14CC4B4BF69":
                drawableId = R.drawable.flag_argentina;
                break;
        }

        return drawableId;
    }
}