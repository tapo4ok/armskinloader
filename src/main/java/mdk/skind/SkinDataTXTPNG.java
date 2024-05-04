package mdk.skind;

import mdk.debug.GUI;
import mdk.mop.Mop;
import mdk.mop.network.ModNetworkHandler;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.util.Properties;

public class SkinDataTXTPNG implements IskinObject {
    private String name;
    private String model;
    private BufferedImage buffer;
    private String ur;

    public SkinDataTXTPNG(String name, String ur) {
        this.name = name;
        this.ur = ur;
        load();
    }

    private boolean loadet1 = false;


    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public int format_version() {
        return 0;
    }

    @Override
    public BufferedImage getSkin() {
        return buffer;
    }

    @Override
    public String getModel() {
        return model;
    }

    @Override
    public int type() {
        return 0;
    }

    @Override
    public void load() {
        File file = new File("skinker");
        if (file.exists()) {
            File file2 = new File(file, name+".png");
            File file1 = new File(file, name+".txt");
            {
                try {
                    BufferedImage buf = ImageIO.read(file2);
                    Properties properties = new Properties();
                    try (FileInputStream fis = new FileInputStream(file1)) {
                        properties.load(fis);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    model=properties.getProperty("model");
                    buffer=buf;
                    loadet1 = true;
                } catch (Exception e) {
                    e.printStackTrace();
                    loadet1 = false;
                }
            }
        }
    }

    @Override
    public void save() {
        Properties properties;
        try {
            properties = new Properties();
            properties.put("model", model);
            properties.save(new FileOutputStream(new File(Mop.instance.file, name+".txt")), "");
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            URL url = new URL(ur);
            InputStream inputStream = url.openStream();
            OutputStream outputStream = new FileOutputStream(new File(Mop.instance.file, name+".png"));

            byte[] buffer = new byte[2048];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, length);
            }

            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public boolean loadet() {
        return loadet1;
    }
}
