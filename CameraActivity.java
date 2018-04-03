public static final int REQUEST_IMAGE_CAPTURE = 1;

private void takePicture() {

        if (isIntentAvailable(this,MediaStore.ACTION_IMAGE_CAPTURE)) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(intent, Constants.REQUEST_IMAGE_CAPTURE);
        }

    }

    public static boolean isIntentAvailable(Context context, String action) {
        final PackageManager packageManager = context.getPackageManager();
        final Intent intent = new Intent(action);
        List<ResolveInfo> list =
                packageManager.queryIntentActivities(intent,PackageManager.MATCH_DEFAULT_ONLY);
        return list.size()>0;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode==Constants.REQUEST_IMAGE_CAPTURE)
            setGroceryImage((Bitmap)data.getExtras().get("data"));
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void takePictureWrapper() {
        int hasCameraPermission = checkSelfPermission(Manifest.permission.CAMERA);
        if (hasCameraPermission != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                showMessageOKCancel("You need to allow access to Camera",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[] {Manifest.permission.CAMERA},
                                        REQUEST_CODE_ASK_PERMISSIONS);
                            }
                        });
                return;
            }
            requestPermissions(new String[] {Manifest.permission.CAMERA},
                    REQUEST_CODE_ASK_PERMISSIONS);
            return;
        }
        takePicture();
    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(GroceryActivity.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_PERMISSIONS:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission Granted
                    takePicture();
                } else {
                    // Permission Denied
                    Toast.makeText(GroceryActivity.this, "CAMERA Denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    public static Image saveToInternalStorage(Context context, String imageName, Bitmap bitmap) {
        String filePath;

        do {
            filePath=saveToInternalStorage(context,imageName,bitmap);
        } while (filePath==null);

        Image value = new Image(imageId,filePath);
        value.save();
        return value;
    }
    public static String saveToInternalStorage(Context context, String imageName, Bitmap bitmap) {
        ContextWrapper cw = new ContextWrapper(context);
        File imageDir = cw.getDir("imageDir",Context.MODE_PRIVATE);
        File imageFile;

        try {
            imageFile = new File(imageDir,imageId + ".png");
        } catch (Exception ex) {
            imageFile=null;
            ex.printStackTrace();
        }

        FileOutputStream stream = null;
        try {
            stream = new FileOutputStream(imageFile);
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
        return (imageFile!=null)?imageFile.getAbsolutePath():"";
    }