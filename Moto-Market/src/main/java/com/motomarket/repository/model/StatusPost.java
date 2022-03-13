package com.motomarket.repository.model;

public enum StatusPost {
    WAITING,PUBLIC, HIDE,SOLD,DELETE,BLOCKED;

//    WAITING: Bài viết mới, cần admin duyệt để hiển thị trên trang chủ
//    PUBLIC: Bài viết đã được duyệt, đang được hiển thị
//    HIDE: Bài viết đang tạm ẩn
//    SOLD: Bài viết đã bán
//    DELETE: Xóa mềm
//    BLOCKED: Những bài đăng public sẽ thành blocked khi user bị block
}
