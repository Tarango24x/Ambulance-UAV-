package androidx.constraintlayout.solver.widgets;

public class Rectangle {
    public int height;
    public int width;

    /* renamed from: x */
    public int f255x;

    /* renamed from: y */
    public int f256y;

    public void setBounds(int x, int y, int width2, int height2) {
        this.f255x = x;
        this.f256y = y;
        this.width = width2;
        this.height = height2;
    }

    /* access modifiers changed from: package-private */
    public void grow(int w, int h) {
        this.f255x -= w;
        this.f256y -= h;
        this.width += w * 2;
        this.height += h * 2;
    }

    /* access modifiers changed from: package-private */
    public boolean intersects(Rectangle bounds) {
        return this.f255x >= bounds.f255x && this.f255x < bounds.f255x + bounds.width && this.f256y >= bounds.f256y && this.f256y < bounds.f256y + bounds.height;
    }

    public boolean contains(int x, int y) {
        return x >= this.f255x && x < this.f255x + this.width && y >= this.f256y && y < this.f256y + this.height;
    }

    public int getCenterX() {
        return (this.f255x + this.width) / 2;
    }

    public int getCenterY() {
        return (this.f256y + this.height) / 2;
    }
}
