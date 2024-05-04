package mdk.skind;

import java.awt.image.BufferedImage;

public interface IskinObject {
    int format_version();
    int type();
    BufferedImage getSkin();
    String getName();
    String getModel();
    void load();
    void save();
    void setModel(String model);
    boolean loadet();
}
