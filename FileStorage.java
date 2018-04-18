    public static String saveToInternalStorage(Context context, String filename, Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(context);
        File fileDir = cw.getDir("imageDir",Context.MODE_PRIVATE);
        File file;

        try {
            file = new File(imageDir,filename + ".png");
        } catch (Exception ex) {
            file=null;
            ex.printStackTrace();
        }

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                stream.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return (file!=null)?file.getAbsolutePath():"";
    }