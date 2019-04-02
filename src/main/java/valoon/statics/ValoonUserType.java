package valoon.statics;

public enum ValoonUserType {

    DEALER,
    CLIENT;

    @Override
    public String toString() {
        return this.name().toLowerCase();
    }
}
