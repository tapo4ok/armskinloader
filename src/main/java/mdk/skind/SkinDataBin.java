package mdk.skind;

import mdk.mop.Mop;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;

public class SkinDataBin implements IskinObject {
    private String name;
    private String model;
    private BufferedImage buffer;
    private String ur;
    private boolean loadet1 = false;

    public SkinDataBin(String name, String ur) {
        this.name = name;
        this.ur = ur;
        load();
    }


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
        try {
            InputStream inputStream = new FileInputStream(new File(Mop.instance.file, name+".bin"));
            DataInputStream dataInputStream = new DataInputStream(inputStream);

            name = dataInputStream.readUTF();
            model = dataInputStream.readUTF();

            int imageLength = dataInputStream.readInt();
            byte[] image = new byte[imageLength];
            dataInputStream.readFully(image);

            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(image);
            buffer = ImageIO.read(byteArrayInputStream);

            inputStream.close();
            loadet1 = true;
        } catch (IOException e) {
            e.printStackTrace();
            loadet1 = false;
        }
    }

    @Override
    public void save() {
        try {
            OutputStream outputStream = new FileOutputStream(new File(Mop.instance.file, name+".bin"));
            DataOutputStream dataOutputStream = new DataOutputStream(outputStream);

            dataOutputStream.writeUTF(name);
            dataOutputStream.writeUTF(model);

            URL url = new URL(ur);
            InputStream inputStream = url.openStream();

            ByteArrayOutputStream outputStream1 = new ByteArrayOutputStream();

            byte[] buffer = new byte[2048];
            int length;

            while ((length = inputStream.read(buffer)) != -1) {
                outputStream1.write(buffer, 0, length);
            }

            outputStream1.close();
            byte[] image = outputStream1.toByteArray();
            dataOutputStream.writeInt(image.length);
            dataOutputStream.write(image);

            inputStream.close();
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
