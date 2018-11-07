package com.mosy.kalin.mosy.Helpers;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;

import com.mosy.kalin.mosy.R;

public class DrawableHelper {

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
                drawableId = R.drawable.filter_fggray_bgmain_toys_corner;
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

            case "75137D9F-FB72-412C-A2FD-163436A41AA7":
                drawableId = R.drawable.filter_fgwhite_bgmain_babyfriendly;
                break;
            case "D64B4596-86CF-4F5A-B88C-17EB9A6630DB":
                drawableId = R.drawable.filter_fggray_bgmain_24hours;
                break;
            case "9834B6DA-14DD-460A-9633-1A5907473E29":
                drawableId = R.drawable.filter_fggray_bgmain_outdoor_sitting;
                break;
            case "3B7C66E5-8F8B-4091-BC4E-216618F553D9":
                drawableId = R.drawable.filter_fggray_bgmain_live_music;
                break;
            case "657C6B9D-A8CC-4767-A972-2BFE6A3481BF":
                drawableId = R.drawable.filter_fggray_bgmain_quiet_music;
                break;
            case "69ABA68E-56D0-4584-9B05-2C20CC30056A":
                drawableId = R.drawable.filter_fggray_bgmain_pub_games;
                break;
            case "9A9D9045-B356-4A2D-886A-42EA52D2C62B":
                drawableId = R.drawable.filter_fggray_bgmain_live_sports;
                break;
            case "3D725220-451D-4FCF-A36A-49A794569CC8":
                drawableId = R.drawable.filter_fggray_bgmain_couches;
                break;
            case "30AF6432-8A43-4C16-A4E3-55A74565D515":
                drawableId = R.drawable.filter_fggray_bgmain_traditional;
                break;
            case "381B30DD-5023-48D5-AFBA-7AF618AB5EF4":
                drawableId = R.drawable.filter_fggray_bgmain_art;
                break;
            case "2E37A4B1-116D-4ED4-8CCE-7E0F66EB2D03":
                drawableId = R.drawable.filter_fggray_bgmain_shisha;
                break;
            case "C33C8B41-FED4-44DC-9B9A-A5C89DD78D0E":
                drawableId = R.drawable.filter_fggray_bgmain_private;
                break;
            case "00AC4D76-BB97-4C37-8226-B4FFC710C219":
                drawableId = R.drawable.filter_fggray_bgmain_dancing;
                break;
            case "68FE1712-78E9-4067-8B03-BB696BBAD92D":
                drawableId = R.drawable.filter_fggray_bgmain_cosy;
                break;
            case "2AA43519-E136-4B8D-BFF9-C0EBD9655BDF":
                drawableId = R.drawable.filter_fggray_bgmain_rooftop;
                break;
            case "996A19E1-D547-4A05-A31F-D066F7760937":
                drawableId = R.drawable.filter_fggray_bgmain_self_service;
                break;
            case "9614795F-256A-4A17-94DA-D6525DC9E2E8":
                drawableId = R.drawable.filter_fggray_bgmain_standing_desks;
                break;
            case "5AB9966D-122D-49EC-9176-F64193E331E7":
                drawableId = R.drawable.filter_fggray_bgmain_standing_desks;
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

//                Dish Type
            case "A9C13F74-219A-4FC5-8D81-0A60D7A1173B":
                drawableId = R.drawable.dish_filter_vegan_food_96;
                break;
            case "6A87D0EA-1503-4B99-8998-22D24C17307E":
                drawableId = R.drawable.dish_filter_spaghetti_96;
                break;
            case "E48B981A-5146-44AB-A78D-2A0029880286":
                drawableId = R.drawable.dish_filter_glass_wine;
                break;
            case "84AD4941-A8FA-45A7-9A9B-3C2D9F4278F5":
                drawableId = R.drawable.dish_filter_sushi_96;
                break;
            case "E8ACF3CB-2C7A-4C8C-A142-5286388EB2AE":
                drawableId = R.drawable.dish_filter_tapas_96;
                break;
            case "71539C9A-7DC6-4C55-860B-528C54DBF80D":
                drawableId = R.drawable.dish_filter_hamburger;
                break;
            case "9F704161-7D3B-4397-A68A-60412FB6A941":
                drawableId = R.drawable.dish_filter_cup_water;
                break;
            case "09BC29A1-A475-4780-85D8-840DFCD3B18D":
                drawableId = R.drawable.dish_filter_food_croissant;
                break;
            case "A47E2E84-E190-4B45-92B9-787669050168":
                drawableId = R.drawable.dish_filter_pizza;
                break;
            case "48CC81A4-C2DE-40E8-A698-B9FE4C15B307":
                drawableId = R.drawable.dish_filter_kitchen_96;
                break;
            case "9412FB28-4236-4202-A817-C35F25CE3B30":
                drawableId = R.drawable.dish_filter_tableware_96;
                break;
            case "672A7F77-8087-4B62-BCC3-ECCDE5774B60":
                drawableId = R.drawable.dish_filter_taco;
                break;
            case "58B436F4-7280-4135-8A9B-F87A940B5C7A":
                drawableId = R.drawable.dish_filter_cupcake;
                break;

//                Dish Region
            case "DDA95DC5-22EB-43A4-A0B2-AB1FAAFB370B":
                drawableId = R.drawable.flag_italy;
                break;
//            case "15E21580-9A42-409B-A0DC-F3182E719322":
//                drawableId = R.drawable.flag_argentina;
//                break;
//            case "341ACCCF-2E5D-4A85-B807-D8628B7AB7E8":
//                drawableId = R.drawable.flag_argentina;
//                break;
//            case "A3308025-F0A2-48EA-BE68-D98EEEB6E453":
//                drawableId = R.drawable.flag_argentina;
//                break;
//            case "DE6A12C1-AC48-439A-A11A-E4CB73DA9EFA":
//                drawableId = R.drawable.flag_argentina;
//                break;
//            case "8148393B-7D8B-485A-9A3C-A7FACBA43F56":
//                drawableId = R.drawable.flag_argentina;
//                break;
//            case "1CA79DC5-56BE-40A1-94AE-900FB9E5CA8D":
//                drawableId = R.drawable.flag_argentina;
//                break;
            case "18A95D3A-6798-4E1F-8387-9537F5CB11D9":
                drawableId = R.drawable.flag_japan;
                break;
//            case "34E27698-00C3-4FD8-9E34-57A855538EFC":
//                drawableId = R.drawable.flag_argentina;
//                break;
//            case "581C594E-6767-4865-BF2A-3AF612DE19D6":
//                drawableId = R.drawable.flag_argentina;
//                break;
//            case "E8CADEA7-E958-4B67-8F45-0584BE8E458D":
//                drawableId = R.drawable.flag_argentina;
//                break;
//            case "9EF7E126-7F8D-4B3F-B363-0949E0C312CE":
//                drawableId = R.drawable.flag_argentina;
//                break;
            case "61B4603E-4E9C-4900-8FD8-12550BC95554":
                drawableId = R.drawable.flag_china;
                break;
            case "4A802883-6164-496E-B780-13FA128F97D8":
                drawableId = R.drawable.flag_india;
                break;

//                Dish Main Ingredient
            case "1EA9ED64-2B59-43ED-B573-35917A858536":
                drawableId = R.drawable.dish_filter_fish_food_96;
                break;
            case "BCABA7FD-2A03-4CD4-853D-51124A791113":
                drawableId = R.drawable.dish_filter_thanksgiving_96;
                break;
            case "72B7A9F9-A4FD-4FA3-8988-6A4FC24BC275":
                drawableId = R.drawable.dish_filter_vegan_symbol_96;
                break;
            case "CFC9FD64-B711-48D3-923D-8D7720451D69":
                drawableId = R.drawable.dish_filter_kosher_food;
                break;
            case "98FBB81E-DE48-4CD9-9E8C-8FEEADA03370":
                drawableId = R.drawable.dish_filter_no_gluten_96;
                break;
            case "9397AB36-37F5-4D70-B021-A971D7221EB8":
                drawableId = R.drawable.dish_filter_steak_96;
                break;
            case "C89C5CAD-2497-407E-A729-E9F8AD14E7EB":
                drawableId = R.drawable.dish_filter_raw_food;
                break;
            case "E576F7AB-3F06-451F-8E36-EF8AC878BED5":
                drawableId = R.drawable.dish_filter_vegetarian_food_symbol_96;
                break;
            case "34461B9A-FBFC-4A89-B67F-F2CFACE03AB0":
                drawableId = R.drawable.dish_filter_halal_food;
                break;

//                Dish Allergen
            case "39DDC3C7-0DE7-4509-BFC6-F8ED357BC12D":
                drawableId = R.drawable.dish_filter_no_mustard_96;
                break;
            case "42AE7A6F-1575-4D1F-A34E-F018A500363A":
                drawableId = R.drawable.dish_filter_hazelnut_96;
                break;
            case "D7390D15-4D85-413D-B600-D75FDB3D47C5":
                drawableId = R.drawable.dish_filter_chili_pepper_96;
                break;
            case "C1357217-4C8C-4F8C-8A3C-BBD1DB4EE071":
                drawableId = R.drawable.dish_filter_no_crustaceans_96;
                break;
            case "98AA2C63-2111-467A-85D4-954125C1B2F1":
                drawableId = R.drawable.dish_filter_peanuts_96;
                break;
            case "83BCC4B1-F594-4929-A1E9-99AC74C9E109":
                drawableId = R.drawable.dish_filter_mushroom_96;
                break;
            case "6551FEDB-2FD5-4BE6-A6EE-81D86B6E89F0":
                drawableId = R.drawable.dish_filter_celery_24;
                break;
            case "43888391-4A4C-4A8C-8F82-870A27E598EF":
                drawableId = R.drawable.dish_filter_glass_wine;
                break;
            case "24017BDD-FC90-4721-A3C6-8778156BD0D6":
                drawableId = R.drawable.dish_filter_shellfish_96;
                break;
            case "5D8B664C-D6E8-4001-9137-879BFA9BE915":
                drawableId = R.drawable.dish_filter_java_bean_96;
                break;
            case "20281739-05BC-4759-82BB-8915CCCE70F9":
                drawableId = R.drawable.dish_filter_eggs_96;
                break;
            case "09691AFA-C81B-494E-A2B9-75B868BC547F":
                drawableId = R.drawable.dish_filter_citrus_96;
                break;
            case "8172C737-AF8E-4BBA-9860-593AD81F99F3":
                drawableId = R.drawable.dish_filter_onion_96;
                break;
            case "35C59700-9D99-49E9-9989-5E06AF952885":
                drawableId = R.drawable.dish_filter_sesame_96;
                break;
            case "450459AB-34B7-4C22-B916-567D6072B420":
                drawableId = R.drawable.dish_filter_no_lupines_96;
                break;
            case "FAEC1D28-FADF-4A25-905C-02D85105D5C4":
                drawableId = R.drawable.dish_filter_soda_bottle_96;
                break;
            case "70993FAA-DB93-4D47-970B-47E6E2D12AC1":
                drawableId = R.drawable.dish_filter_java_bean_96;
                break;
            case "96B7AD17-FE4B-44B4-8989-337EA6771736":
                drawableId = R.drawable.dish_filter_soy_96;
                break;
            case "44B7A122-16F0-4A9D-A2EB-34BABED8384A":
                drawableId = R.drawable.dish_filter_fish_food_96;
                break;
            case "F5950E87-6133-42E0-8D98-1F11C5DF1F18":
                drawableId = R.drawable.dish_filter_milk_bottle_24;
                break;
            case "209DFDFB-0B28-43A9-B5E9-1F17CA4F91C1":
                drawableId = R.drawable.dish_filter_garlic_96;
                break;
            case "85ECCB52-E463-40AA-BC5B-0EA5F9754C04":
                drawableId = R.drawable.dish_filter_wheat_96;
                break;


        }

        return drawableId;
    }

    public static void changeTint(Drawable drawable, int colorResourceId, boolean mutate){
        if (drawable != null) {
            // If we don't mutate the drawable, then all drawable's with this id will have a color
            // filter applied to it.
            if (mutate)
                drawable.mutate();
            drawable.setColorFilter(colorResourceId, PorterDuff.Mode.SRC_ATOP);
        }
    }
}