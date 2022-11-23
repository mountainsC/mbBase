//链接或web分享
ShareDialogManager shareDialogManager = ShareDialogManager.getInstance();
shareDialogManager.setOnShareSuccessCall(new OnShareSuccessCall() {
    @Override
    public void onShareSuccess(SHARE_MEDIA platform) {

    }
});
ShareContent shareContent = new ShareContent();
shareContent.setContent("双12");
shareContent.setLogo("https://gss0.bdstatic.com/7Ls0a8Sm1A5BphGlnYG/sys/portrait/item/2f17e699b4e5bdb133f02c.jpg");
shareContent.setTitle("文章");
shareContent.setUrl("https://zhidao.baidu.com/question/420055806.html");
File dir = StorageUtils.getDataCachesDir();
shareContent.setFile(new File(dir, "111_2.xlsx"));
shareContent.setShareType(ShareType.image);
shareDialogManager.setShareContent(shareContent);
shareDialogManager.build(getActivity());
shareDialogManager.show();