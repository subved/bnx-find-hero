package com.company.project.tools;

public class CareerParseTool {

    public static String careerParse(String careerAddress){
        //游侠 主属性力量,副属性敏捷
        if("0xF31913a9C8EFE7cE7F08A1c08757C166b572a937".equals(careerAddress)){
            return "游侠";
        }
        //法师 主属性智力,副属性精神
        else if("0xC6dB06fF6e97a6Dc4304f7615CdD392a9cF13F44".equals(careerAddress)){
            return "法师";
        }
        //战士 主属性力量,副属性体质
        else if("0x22F3E436dF132791140571FC985Eb17Ab1846494".equals(careerAddress)){
            return "战士";
        }
        //盗贼 主属性敏捷,副属性力量
        else if("0xaF9A274c9668d68322B0dcD9043D79Cd1eBd41b3".equals(careerAddress)){
            return "盗贼";
        }
        //德鲁伊 主属性智力,副属性体质
        else if("0x1505Fc2ca150971bA8f254771359e50bCF26610f".equals(careerAddress)){
            return "德鲁伊";
        }
        //牧师 主属性智力,副属性意志
        else if("0x579A3e1C40124b6bcb3d244bb9a0A816aeD0c78D".equals(careerAddress)){
            return "牧师";
        }
        //骑士 主属性体质,副属性力量
        else if("0x7773bd9b39989F9610B17Ca5972546BD475b8FAa".equals(careerAddress)){
            return "骑士";
        }

        return "";

    }
}
