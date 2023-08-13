package hello.itemservice.domain.item;

public enum ItemType {

    BOOK("도서"), FOOD("음식"), ETC("기타");

    private final String description; // 설명

    ItemType(String description) {
        this.description = description;
    }

    // 데이터를 읽을 수 있게 getter추가

    public String getDescription() {
        return description;
    }
}
