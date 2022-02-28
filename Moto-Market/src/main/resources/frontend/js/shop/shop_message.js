/**
* Function common show popup for other branch
*/
function popupMessage(target, width = 500){
    if (target.length) {
        // trigger popup for map shop
        if ('fancybox' in target) {
            target.fancybox({
                padding: 0,
                modal: true,
                width: width,
                autoSize: false,
                autoHeight: true,
                wrapCSS: 'modal'
            });
        }
    }
}

$('.check-viewed').click(function(){
    var self = $(this);
    var closest = self.closest('li');
    var url = $('#ajaxViewMessage').val();
    var shop_message_id = closest.find('.shop-message-id').val();

    $.ajax({
        'url': url,
        'type': 'POST',
        'dataType': 'json',
        'data':{
            'shop_message_id': shop_message_id
        },
        'success': function(result){
            if(result.valid){
                window.location.reload();
            }
        }
    });
});

$('.delete-message').click(function(){
    var self = $(this);
    var closest = self.closest('li');
    var url = $('#ajaxDelMessage').val();
    var shop_message_id = closest.find('.shop-message-id').val();
    
    var html = closest.find('.btn-delete-noti').html();
    if(confirm('Bạn có muốn xóa?')){
        $.ajax({
            'url': url,
            'type': 'POST',
            'dataType': 'json',
            'data':{
                'shop_message_id': shop_message_id
            },
            'success': function(result){
                if(result){
                    closest.remove();
                }
            }
        });
    }
});