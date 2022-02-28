// JavaScript Document
var url_post_package_user = $('#url_post_package_user').val();

function click_package_vip(){
    var package_id = $('#select-package-vip').val();
    submit_package(package_id ,'#btn-package-vip', '#btn-package-free-trial, #btn-package-premium');
}

function click_package_premium(){
    var package_id = $('#select-package-premium').val();
    submit_package(package_id ,'#btn-package-premium', '#btn-package-free-trial, #btn-package-vip');
}

function click_package_free_trial(package_id){
    submit_package(package_id ,'#btn-package-free-trial', '#btn-package-premium, #btn-package-vip');
}

function submit_package(package_id){
//function submit_package(package_id, primary_btn, extra_btn){
    $.ajax({
        'url' : url_post_package_user,
        'type' : 'POST',
        'dataType' : 'JSON',
        'data' : {
            'package_id' : package_id,
            'waiting' : true,
        },
        'success' : function(data){
            if(data.waiting){
                window.location.reload();
//                $(extra_btn).removeClass('module-btn').attr('disabled', true);
//                $(primary_btn).val('Chờ kích hoạt...');
//                showNotify(data);
            }
        }
    }); 
}

function showNotify(data){
    $('#content select').attr('disabled', true);
    $('#content input[type="button"]').removeAttr('onclick');
    $('#notify').html(data.notifyWaiting);
}

// for PC
$('.btn-submit-package').click(function(){
    var 
    self = $(this),
    head = self.closest('div.item-package'),
    value = head.find('.select-package').val();
    
    if(confirm('Bạn đồng ý chọn gói dịch vụ này?')){
        submit_package(value);
    }
});

// for Mobile
$('.btn-submit-mobile-package').click(function(){
    var 
    self = $(this),
    head = self.closest('div.item'),
    value = head.find('.select-package').val();
    
    if(confirm('Bạn đồng ý chọn gói dịch vụ này?')){
        submit_package(value);
    }
});