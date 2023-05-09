public class Link {

    int download;
    int upload;
    int cost;

    public Link(int download, int upload, int cost) {
        this.download = download;
        this.upload = upload;
        this.cost = cost;
    }

    public int getDownload() {
        return download;
    }

    public void setDownload(int download) {
        this.download = download;
    }

    public int getUpload() {
        return upload;
    }

    public void setUpload(int upload) {
        this.upload = upload;
    }

    public int getCost() {
        return cost;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }
}
