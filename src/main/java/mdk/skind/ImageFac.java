package mdk.skind;

import mdk.config.ServerConfigs;

public class ImageFac {
    public static IskinObject get(String name, String ur) {
        if (ServerConfigs.file_format==0) {
            return new SkinDataTXTPNG(name, ur);
        } else {
            return new SkinDataBin(name, ur);
        }
    }
}
