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
        }

        switch (filterId) {
            case "f70b7313-b85b-4d87-86f4-5f8622ed67d8": //'Afghanistan'
                drawableId = R.drawable.flag_afghanistan;
                break;
            case "1cb5d2d9-e8ac-418b-bd16-deab30e8f136": //'Albania'
                drawableId = R.drawable.flag_albania;
                break;
            case "43cbfa7b-b6ed-41ad-afe3-99e11e4d8a1d": //'Algeria'
                drawableId = R.drawable.flag_algeria;
                break;
            case "c42e0364-a1e9-4ab0-a8b3-630ed1f787bb": //'Andorra'
                drawableId = R.drawable.flag_andorra;
                break;
            case "86cdfb4c-6b9b-4851-9b03-6694f2b8705b": //'Antigua and Babuda'
                drawableId = R.drawable.flag_antigua_and_barbuda;
                break;
            case "1a4dfd60-dcf7-4101-a8b9-bbc2478f32ce": //'Armenia'
                drawableId = R.drawable.flag_armenia;
                break;
            case "68837089-97a0-4e28-b48a-f47b1f7a12eb": //'Australia'
                drawableId = R.drawable.flag_australia;
                break;
            case "10423ac8-295e-4500-9cbb-0746faa1fedf": //'Austria'
                drawableId = R.drawable.flag_austria;
                break;
            case "d48d517a-68be-4622-8290-96d8c8e31851": //'Azerbaijan'
                drawableId = R.drawable.flag_azerbaijan;
                break;
            case "03de5632-fc19-4180-9ba1-f3520e7f2afc": //'Bahamas'
                drawableId = R.drawable.flag_bahamas;
                break;
            case "02ce098a-61ad-4262-8811-e8ff20d8860e": //'Bahrain'
                drawableId = R.drawable.flag_bahrain;
                break;
            case "d35b8866-8904-4cb4-b0f1-a9a643f40936": //'Bangladesh'
                drawableId = R.drawable.flag_bangladesh;
                break;
            case "486d06e6-2510-4ff5-a33b-29227e86ef76": //'Barbados'
                drawableId = R.drawable.flag_barbados;
                break;
            case "828091e1-912c-418b-8178-93caf7cb19dd": //'Belarus'
                drawableId = R.drawable.flag_belarus;
                break;
            case "d6fcde43-00c4-4ee3-8401-35504d603969": //'Belgium'
                drawableId = R.drawable.flag_belgium;
                break;
            case "cde6097c-1e42-430b-b288-12f8f7a1d2ff": //'Belize'
                drawableId = R.drawable.flag_belize;
                break;
            case "0aac2049-5050-40b6-ba6f-521aedc7cbea": //'Benin'
                drawableId = R.drawable.flag_benin;
                break;
            case "52357c52-718d-4147-bd09-2125634d2bae": //'Bhutan'
                drawableId = R.drawable.flag_bhutan;
                break;
            case "c9dc5bc2-7fb6-4051-9ad8-e101712b3a4d": //'Bolivia'
                drawableId = R.drawable.flag_bolivia;
                break;
            case "d4b87402-8ac8-4bf0-8b39-f9c7a8d26700":
                drawableId = R.drawable.flag_bosnia_and_herzegovina;//'Bosnia and Herzegovina'
                break;
            case "ee654682-e28b-400c-b715-7a1188750373":
                drawableId = R.drawable.flag_botswana;//'Botswana'
                break;
            case "d8ce85d8-61a8-469c-a42f-1b2b9d0c29f2":
                drawableId = R.drawable.flag_zimbabwe;//'Zimbabwe'
                break;
            case "a7f3098b-a4d2-41a1-a940-b1d80ffbe83e":
                drawableId = R.drawable.flag_brunei;//'Brunei'
                break;
            case "73ad23d6-c0fe-4c76-ae53-9621224f03f7":
                drawableId = R.drawable.flag_burkina_baso;//'Burkina Faso'
                break;
            case "ecfd4262-dcb7-4ca0-b791-163edfcef3e4":
                drawableId = R.drawable.flag_burundi;//'Burundi'
                break;
            case "6b415603-67e9-4a11-aebe-633e3fe7aded":
                drawableId = R.drawable.flag_cambodia;//'Cambodia'
                break;
            case "503354bf-3c80-4a0b-8bbf-d4abbe505cc4":
                drawableId = R.drawable.flag_cameroon;//'Cameroon'
                break;
            case "45659633-5f6a-49c0-bb7e-38b11b88d2d1":
                drawableId = R.drawable.flag_canada;//'Canada'
                break;
            case "fb0eab8f-8740-468d-8ac6-9a2be483b48a":
                drawableId = R.drawable.flag_cape_verde;//'Cape Verde'
                break;
            case "514d039e-346e-4b24-8084-805d30a46fbb":
                drawableId = R.drawable.flag_central_african_republic;//'Central African Republic'
                break;
            case "8fee1047-69c8-4b95-b758-cffdb064107c":
                drawableId = R.drawable.flag_chad;//'Chad'
                break;
            case "f877a442-f9a9-4285-afe2-e4b80f82a3ea":
                drawableId = R.drawable.flag_chile;//'Chile'
                break;
            case "d080165e-0019-4e5d-a44c-d184189ffdbe":
                drawableId = R.drawable.flag_china;//'China'
                break;
            case "daf67774-e58e-42d1-ab81-9540af479d7c":
                drawableId = R.drawable.flag_comoros;//'Comoros'
                break;
            case "4df5b81d-c25a-4657-8e30-401ca9b5bc1c":
                drawableId = R.drawable.flag_congo;//'Congo'
                break;
            case "8028c88e-3c53-434a-9da0-920f34a43ee3":
                drawableId = R.drawable.flag_congo_democratic;//'Congo Democratic'
                break;
            case "ab29d06d-1358-4fe6-938f-a0d1c5675704":
                drawableId = R.drawable.flag_costa_rica;//'Costa Rica'
                break;
            case "26d79bfd-2e70-4962-ae08-a1372174ff60":
                drawableId = R.drawable.flag_cote_d_ivoire;//'Cot d Ivoir'
                break;
            case "758d65db-66bc-4506-8822-bba1508d638e":
                drawableId = R.drawable.flag_croatia;//'Croatia'
                break;
            case "18a764d8-bdce-46a0-97ee-a6b78bfe31aa":
                drawableId = R.drawable.flag_cyprus;//'Cyprus'
                break;
            case "6d6784d4-083d-4392-b5cc-a9f17ee200c2":
                drawableId = R.drawable.flag_czech_republic;//'Czech Republic'
                break;
            case "f9c2eca4-2f51-40dd-915d-ab760e7e1646":
                drawableId = R.drawable.flag_denmark;//'Denmark'
                break;
            case "db1263c5-8309-4b74-b2fd-01afa9381f38":
                drawableId = R.drawable.flag_djibouti;//'Djibouti'
                break;
            case "5933e228-a104-4139-a76e-c340d100c9ec":
                drawableId = R.drawable.flag_dominica;//'Dominica'
                break;
            case "47c8fc78-ea2c-4a75-abc2-294a84940b20":
                drawableId = R.drawable.flag_dominican_republic;//'Dominican Republic'
                break;
            case "6892c483-72cd-4e3d-9afb-605a8fdb3cc9":
                drawableId = R.drawable.flag_east_timor;//'East Timor
                break;
            case "e68d0ef6-e115-420c-b8d7-ceec170eb392":
                drawableId = R.drawable.flag_ecuador;//'Ecuador'
                break;
            case "69427cae-8ca8-44aa-a905-e027f78e3758":
                drawableId = R.drawable.flag_egypt;//'Egypt'
                break;
            case "0e62c17c-3727-4cbf-a1f0-a061706eb80f":
                drawableId = R.drawable.flag_el_salvador;//'El Salvador
                break;
            case "f3d40998-cc40-41d3-9cab-ce39474bc1d5":
                drawableId = R.drawable.flag_equatorial_guinea;//'Equatorial Guinea
                break;
            case "87dba918-3343-4571-9bc4-bc67d2a04643":
                drawableId = R.drawable.flag_eritrea;//'Eritrea
                break;
            case "7db4cbe2-b52b-47aa-8360-c1228a919384":
                drawableId = R.drawable.flag_estonia;//'Estonia
                break;
            case "240867a6-3119-4fca-ba35-0cdd9ca0adb1":
                drawableId = R.drawable.flag_ethiopia;//'Ethiopia
                break;
            case "59ca6cac-48ba-45a1-8478-218ca34508b8":
                drawableId = R.drawable.flag_fiji;//'Fiji
                break;
            case "ba30252b-e986-4553-ad90-1dc077613a65":
                drawableId = R.drawable.flag_finland;//'Finland
                break;
            case "53b781bd-7070-4c21-8c9b-9a28689b961f":
                drawableId = R.drawable.flag_france;//'France'
                break;
            case "2910a019-652e-4c69-b5b5-e4f798254644":
                drawableId = R.drawable.flag_gabon;//'Gabon'
                break;
            case "fa6d6a65-eb04-41c5-bed2-5b68b1bb3066":
                drawableId = R.drawable.flag_gambia;//'Gambia
                break;
            case "d7b05365-f40d-4465-a060-7492d05b0e6d":
                drawableId = R.drawable.flag_georgia;//'Georgia
                break;
            case "b3eb7d3d-8fb7-4d6d-8427-2232fc61e22e":
                drawableId = R.drawable.flag_ghana;//'Ghana
                break;
            case "51db1195-b7c6-4293-8349-2692e607c6af":
                drawableId = R.drawable.flag_grenada;//'Grenada
                break;
            case "65f0b93c-e88c-4f98-a87c-9f1d586b4395":
                drawableId = R.drawable.flag_guatemala;//'Guatemala
                break;
            case "54065748-efb0-4118-970c-4ed2a27e58d7":
                drawableId = R.drawable.flag_guinea;//'Guinea
                break;
            case "6ef2f514-11bb-4780-9155-ee4963efb626":
                drawableId = R.drawable.flag_guinea_bissau;//'Guinea Bissau
                break;
            case "814aa59c-b927-49f5-9195-88c04f2ef814":
                drawableId = R.drawable.flag_guyana;//'Guyana'
                break;
            case "43e2c994-7316-4522-8a08-5b85e7a6c2fb":
                drawableId = R.drawable.flag_haiti;//'Haiti'
                break;
            case "8ad833e0-bfd4-4cb1-9410-0ec31b39b51f":
                drawableId = R.drawable.flag_honduras;//'Honduras
                break;
            case "7838c45c-2625-464a-999d-86909a428c0f":
                drawableId = R.drawable.flag_hungary;//'Hungary
                break;
            case "d8236caf-bb33-47e7-b55d-1573a1e9fe3d":
                drawableId = R.drawable.flag_iceland;//'Iceland
                break;
            case "3c5ab5bf-672b-44c7-9664-24bf9187d8bc":
                drawableId = R.drawable.flag_india;//'India
                break;
            case "25abe42e-3564-4123-a85a-53ba326f24d4":
                drawableId = R.drawable.flag_indonesia;//'Indonesia
                break;
            case "716a5885-9960-4b25-becd-54cf9de971d4":
                drawableId = R.drawable.flag_iran;//'Iran
                break;
            case "51f650d0-8d41-4c20-932e-37ad3ca069a4":
                drawableId = R.drawable.flag_iraq;//'Iraq
                break;
            case "9ebaf62c-d929-42b5-9928-257a7aa43ba1":
                drawableId = R.drawable.flag_ireland;//'Ireland
                break;
            case "c41d0dd4-ac6f-49e1-91eb-0eaf23c89a6c":
                drawableId = R.drawable.flag_israel;//'Israel
                break;
            case "b03a8bac-7a4e-4f92-94ec-77fd9b5baaaa":
                drawableId = R.drawable.flag_jamaica;//'Jamaica
                break;
            case "2ba01e1c-db24-489d-985e-0fdb18e361f1":
                drawableId = R.drawable.flag_japan;//'Japan
                break;
            case "d0b54b76-791f-4e5d-91f1-634a69d418e3":
                drawableId = R.drawable.flag_jordan;//'Jordan
                break;
            case "19d1227a-bf8f-4922-8011-55866d214c4b":
                drawableId = R.drawable.flag_kazakhstan;//'Kazakhstan
                break;
            case "26b2d9a3-9331-4fe6-a186-18932a44610f":
                drawableId = R.drawable.flag_kenya;//'Kenya
                break;
            case "02e60e50-556b-4beb-92d6-33cbcd0b5917":
                drawableId = R.drawable.flag_korea_north;//'Korea North
                break;
            case "9c9c996b-f436-4184-880d-57340e81c133":
                drawableId = R.drawable.flag_korea_south;//'Korea South
                break;
            case "299e8433-f2b3-40b9-998f-ebe06a50202b":
                drawableId = R.drawable.flag_kosovo;//'Kosovo
                break;
            case "e3faed2e-a9a4-4212-b7b3-00af8aa91d6a":
                drawableId = R.drawable.flag_kuwait;//'Kuwait
                break;
            case "d53739c2-58f6-4d1a-90d0-0418f001c1fe":
                drawableId = R.drawable.flag_kyrgyzstan;//'Kyrgyzstan
                break;
            case "8c587b59-f803-4b55-80fa-afbd3b1be725":
                drawableId = R.drawable.flag_laos;//'Laos
                break;
            case "c37646de-1b63-4f68-a33d-982b1e4454da":
                drawableId = R.drawable.flag_latvia;//'Latvia
                break;
            case "5c4290a5-8079-4f51-9300-ae9f26531987":
                drawableId = R.drawable.flag_lebanon;//'Lebanon
                break;
            case "07a7c75a-467b-47d9-b799-5ff8f069784b":
                drawableId = R.drawable.flag_lesotho;//'Lesotho
                break;
            case "1c4b95bb-2a00-4082-a500-72cb2f18c875":
                drawableId = R.drawable.flag_liberia;//'Liberia
                break;
            case "597a3468-41c8-49a5-bc94-42f6a1ed815b":
                drawableId = R.drawable.flag_libya;//'Libya
                break;
            case "dc2b17ff-93e5-43f1-9d5c-c02e34321e69":
                drawableId = R.drawable.flag_liechtenstein;//'Liechtenstein
                break;
            case "92881746-1c85-4d49-8a85-5a072a83caf6":
                drawableId = R.drawable.flag_lithuania;//'Lithuenia
                break;
            case "10b881f1-9762-40a2-bd0d-edac41a696b7":
                drawableId = R.drawable.flag_luxembourg;//'Luxembourg
                break;
            case "6d249d7f-f7cd-4bda-9619-22b628ce552a":
                drawableId = R.drawable.flag_macedonia;//'Macedonia
                break;
            case "fedbeb21-d722-4f98-bd42-cfbdcb6fd709":
                drawableId = R.drawable.flag_madagascar;//'Madagascar
                break;
            case "bb1025d0-6123-40c6-af98-bdb52f55a7dc":
                drawableId = R.drawable.flag_malawi;//'Malawi
                break;
            case "d3d2d935-b95f-4043-b301-e2f888a8e2f0":
                drawableId = R.drawable.flag_malaysia;//'Malaysia
                break;
            case "c9914873-8874-444b-aaf9-5e990c821cf1":
                drawableId = R.drawable.flag_maldives;//'Maldives
                break;
            case "afee8fdd-769b-4d8b-8450-30e1d87d69f1":
                drawableId = R.drawable.flag_mali;//'Mali
                break;
            case "59ba708b-f252-4402-bab6-af7f0c62b287":
                drawableId = R.drawable.flag_malta;//'Malta
                break;
            case "ee604471-f30a-4828-8a6a-6bae88afc2de":
                drawableId = R.drawable.flag_marshall_islands;//'Marshal Islands
                break;
            case "db321453-b277-435a-8654-bb04a21a9b3d":
                drawableId = R.drawable.flag_mauritania;//'Maruitania
                break;
            case "3f7954c7-0c7a-4e88-85fb-1b7b93a6caf9":
                drawableId = R.drawable.flag_mauritius;//'Mauritius
                break;
            case "113807b6-0a5d-45e1-809a-c02415a91d13":
                drawableId = R.drawable.flag_mexico;//'Mexico
                break;
            case "6159d929-d13f-423d-a729-a5cdb039eaf9":
                drawableId = R.drawable.flag_micronesia;//'Micronesia
                break;
            case "5307b11b-cc72-45d5-a6b1-fc21b2623593":
                drawableId = R.drawable.flag_moldova;//'Moldova
                break;
            case "a3c463fa-6509-4857-8ba7-44e4323595b0":
                drawableId = R.drawable.flag_monaco;//'Monaco
                break;
            case "57110978-dedc-48f1-8927-a7b422a41d30":
                drawableId = R.drawable.flag_mongolia;//'Mongolia
                break;
            case "bed8a3f0-897e-49c8-8429-a6a2d7088211":
                drawableId = R.drawable.flag_montenegro;//'Montenegro
                break;
            case "8e5f1bbf-2aea-4ed8-96e9-e4c485629800":
                drawableId = R.drawable.flag_morocco;//'Morocco
                break;
            case "d2da9f70-ae5c-4356-8c1e-2cfee0998718":
                drawableId = R.drawable.flag_mozambique;//'Mozambique
                break;
            case "d9bd05a6-f5b8-475e-9dc8-ecebef1475f3":
                drawableId = R.drawable.flag_myanmar;//'Myanmar
                break;
            case "011579f9-72a3-4733-a5ed-8f055c2a5b0b":
                drawableId = R.drawable.flag_namibia;//'Namibia
                break;
            case "29e3a3b5-b830-48d7-baed-0205545dcea4":
                drawableId = R.drawable.flag_nauru;//'Nauru
                break;
            case "8481e694-788c-4d75-8aae-fe1d2e064f21":
                drawableId = R.drawable.flag_nepal;//'Nepal
                break;
            case "94763f01-bf77-427b-a428-9e06145f60f3":
                drawableId = R.drawable.flag_netherlands;//'Neherlands
                break;
            case "36da6164-35d8-46a7-9049-895381fd5038":
                drawableId = R.drawable.flag_new_zealand;//'New Zealand
                break;
            case "5eeb1254-6a86-4add-a412-7c268be3e9b3":
                drawableId = R.drawable.flag_nicaragua;//'Nicaragua
                break;
            case "8dab9688-3457-43d9-b4ac-a6a9d8796f5d":
                drawableId = R.drawable.flag_niger;//'Niger
                break;
            case "b6a86d62-d2bc-43cc-b306-fb1e6ebcba49":
                drawableId = R.drawable.flag_nigeria;//'Nigeria
                break;
            case "78104092-3b76-4f8a-b887-66831a1da5b0":
                drawableId = R.drawable.flag_norway;//'Norway
                break;
            case "c106de48-826c-44a1-8fa3-a1e9179069c7":
                drawableId = R.drawable.flag_oman;//'Oman
                break;
            case "ffbdf0a1-0c51-41fb-a3aa-ecc957061618":
                drawableId = R.drawable.flag_pakistan;//'Pakistan
                break;
            case "11c43925-0b59-4535-8a2f-7462bf41989c":
                drawableId = R.drawable.flag_palau;//'Palau
                break;
            case "829f5f60-ca34-4e64-a3a1-cf322f5bc492":
                drawableId = R.drawable.flag_palestine;//'Palestine
                break;
            case "8e283265-f5c9-41db-b579-acabed64a66f":
                drawableId = R.drawable.flag_panama;//'Panama
                break;
            case "404b0e42-42ec-4212-940b-c3a7391c7c26":
                drawableId = R.drawable.flag_papua_new_guinea;//'Papua New Guinea
                break;
            case "4462592c-c23d-45d9-97d0-cc8e433a9992":
                drawableId = R.drawable.flag_paraguay;//'Paraguay
                break;
            case "5d7cf5e4-bf60-48fa-ac62-8602d01e62f9":
                drawableId = R.drawable.flag_philippines;//'Philipines
                break;
            case "cdc346ad-da5c-4701-9974-3aea72409cae":
                drawableId = R.drawable.flag_poland;//'Poland
                break;
            case "669f571a-61ff-4e8d-8eb6-3871115157a4":
                drawableId = R.drawable.flag_peru;//'Peru
                break;
            case "88bb767a-e48c-4b09-a381-812c305a7faa":
                drawableId = R.drawable.flag_romania;//'Romania
                break;
            case "71e76c8b-5132-4075-a319-5e8447bd9d53":
                drawableId = R.drawable.flag_qatar;//'Qatar
                break;
            case "f2330ec5-7be1-457a-9527-9fe7e23806d7":
                drawableId = R.drawable.flag_rwanda;//'Rwanda
                break;
            case "494c6d2a-1d77-4ede-9308-772b0c53e40f":
                drawableId = R.drawable.flag_saint_kitts_and_nevis;//'Saint Kitts and Nevis
                break;
            case "1765568c-f999-41f1-9b32-735344213dbc":
                drawableId = R.drawable.flag_saint_vincent_and_the_grenadines;//'Saint Vincent and the Grenadines
                break;
            case "b9286072-4c72-4f3a-8258-639183c56ba0":
                drawableId = R.drawable.flag_samoa;//'Samoa
                break;
            case "b70124d9-c857-4b95-a826-1b9e5686122f":
                drawableId = R.drawable.flag_san_marino;//'San Marino
                break;
            case "c81977c7-6fd8-478d-a30b-bdd3ee275867":
                drawableId = R.drawable.flag_sao_tome_and_principe;//'Sao Tome and Principe
                break;
            case "547fccd3-afe0-4b4e-99ca-18dfa9dc61b9":
                drawableId = R.drawable.flag_saudi_arabia;//'Saudi Arabia
                break;
            case "f1b925cb-3a94-4f56-80d9-ba1826055ab7":
                drawableId = R.drawable.flag_senegal;//'Senegal
                break;
            case "314a8e9a-14a7-48fe-b833-cc4cdb10ea5b":
                drawableId = R.drawable.flag_serbia;//'Serbia
                break;
            case "2279cc36-d77c-453d-a701-6a57cc68fb00":
                drawableId = R.drawable.flag_seychelles;//'Seychelles
                break;
            case "cd488de9-7523-4459-9c29-b88cd205663f":
                drawableId = R.drawable.flag_sierra_leone;//'Sierra Leone
                break;
            case "e94a0fc3-a87e-4b87-9bcd-eae9fa1b6170":
                drawableId = R.drawable.flag_singapore;//'Singapore
                break;
            case "9b5b334b-9957-4435-b324-150b80c7355c":
                drawableId = R.drawable.flag_slovakia;//'Slovakia
                break;
            case "fd88d85a-1f7b-4c02-9fda-df529abbb480":
                drawableId = R.drawable.flag_slovenia;//'Slovenia
                break;
            case "460f27bf-d641-40dc-a84f-c6665414d4e1":
                drawableId = R.drawable.flag_solomon_islands;//'Solomon Islands
                break;
            case "0f52516e-4d0e-443a-9540-92cc2081e99e":
                drawableId = R.drawable.flag_somalia;//'Somalia
                break;
            case "5a73b072-adc3-4694-a61f-796334a102fa":
                drawableId = R.drawable.flag_south_africa;//'South Africa
                break;
            case "2200f1d1-acad-48f7-be5d-62c57ae7ae24":
                drawableId = R.drawable.flag_south_sudan;//'South Sudan
                break;
            case "d556daa4-67c8-4b80-b10b-9b26bf671fd3":
                drawableId = R.drawable.flag_sri_lanka;//'Sri Lanka
                break;
            case "e9d374b9-ff01-418d-97ab-da47191a2f94":
                drawableId = R.drawable.flag_sudan;//'Sudan
                break;
            case "7a8db469-e31d-4983-adb2-218376f2ebaf":
                drawableId = R.drawable.flag_suriname;//'Suriname
                break;
            case "3c24ffe7-ec44-49df-8a49-ae3c942e3a29":
                drawableId = R.drawable.flag_swaziland;//'Swaziland
                break;
            case "20bab80b-c34c-4996-a408-fcef7e76f4eb":
                drawableId = R.drawable.flag_sweden;//'Sweden
                break;
            case "1ea93cb7-45ec-45f8-94d2-153e3fbd453e":
                drawableId = R.drawable.flag_switzerland;//'Switzerland
                break;
            case "cf5edf09-4e94-4d65-aefe-aac28d9cc03d":
                drawableId = R.drawable.flag_syria;//'Syria
                break;
            case "2a974407-b726-4464-9666-1771c385fb38":
                drawableId = R.drawable.flag_taiwan;//'Taiwan
                break;
            case "2d659e6d-ef7d-4e87-9e0c-fcff0b135374":
                drawableId = R.drawable.flag_tajikistan;//'Tajikistan
                break;
            case "df3b2db5-d7ed-4b25-af58-8ceba15bc047":
                drawableId = R.drawable.flag_tanzania;//'Tanzania
                break;
            case "1fdfa9da-a1ff-4454-b724-681b2047ae7f":
                drawableId = R.drawable.flag_thailand;//'Thailand
                break;
            case "43157e3d-c27c-48bd-aafd-31242c7ec8f6":
                drawableId = R.drawable.flag_togo;//'Togo
                break;
            case "ba9d3659-8d7c-4db5-9f94-b55da241d792":
                drawableId = R.drawable.flag_tonga;//'Tonga
                break;
            case "2500c216-edf2-4d3a-a2c8-ba998566d5b0":
                drawableId = R.drawable.flag_trinidad_and_tobago;//'Trinidad and Tobago
                break;
            case "06eaab81-8c24-4a0a-8620-d41fcb3a8d13":
                drawableId = R.drawable.flag_tunisia;//'Tunisia
                break;
            case "7aeb1abf-e7e2-4cae-8192-3b366ab8f832":
                drawableId = R.drawable.flag_turkmenistan;//'Turkmenistan
                break;
            case "7648b1ff-dcdb-49e0-8a6c-9b6918017711":
                drawableId = R.drawable.flag_tuvalu;//'Tuvalu
                break;
            case "c34233cb-3385-4360-b801-b947e678b261":
                drawableId = R.drawable.flag_uganda;//'Uganda
                break;
            case "6b40654c-aeac-472a-9275-09b1baf5c728":
                drawableId = R.drawable.flag_ukraine;//'Ukraine
                break;
            case "7ea36feb-8af0-4474-a766-4a0dde5377f8":
                drawableId = R.drawable.flag_united_arab_emirates;//'United Arab Emirates
                break;
            case "e29489f9-9df9-4ae0-8bbf-a7f5870220ac":
                drawableId = R.drawable.flag_united_kingdom;//'United Kingdom
                break;
            case "7984cd34-fa34-4db0-9ef1-14a1ec5be35b":
                drawableId = R.drawable.flag_uruguay;//'Uruguay
                break;
            case "7bdd757e-9dd3-494a-b8c7-c23f60bc3d9f":
                drawableId = R.drawable.flag_usa;//'USA
                break;
            case "f9e51884-27b8-4699-8aba-8898e4d1f1cc":
                drawableId = R.drawable.flag_uzbekistan;//'Uzbekistan
                break;
            case "d4639c64-6ad4-4ebe-8153-5d4e241e9551":
                drawableId = R.drawable.flag_vanuatu;//'Vanuatu
                break;
            case "e85dc8ee-5902-458e-b4c7-6ef5611b5246":
                drawableId = R.drawable.flag_vatican_city;//'Vatican City
                break;
            case "4cd858ed-06fb-4dba-a5f3-e407f9dd2415":
                drawableId = R.drawable.flag_venezuela;//'Venezuela
                break;
            case "3c275434-a535-40bb-b4a6-1b13d09b00b4":
                drawableId = R.drawable.flag_vietnam;//'Vietnam
                break;
            case "46d500d0-dda0-4bda-b130-44e0f8499cd0":
                drawableId = R.drawable.flag_yemen;//'Yemen
                break;
            case "d9e5efe4-53f5-4a7f-889b-23ee83267570":
                drawableId = R.drawable.flag_zambia;//'Zambia
                break;
        }

        switch (filterId.toUpperCase()) {
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