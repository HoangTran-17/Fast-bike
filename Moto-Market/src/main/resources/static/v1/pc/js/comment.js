function updateViewOfComment(comment_id, product_id){
    $.ajax({
        'url' : $('#url_upview_comment').val(),
        'type' : 'POST',
        'dataType' : 'JSON',
        'data' : {
            'comment_id' : comment_id,
            'product_id' : product_id,
        },
        'success' : function(data){
            if(data)
                $('.viewed-' + comment_id).html('<span style="color: #999">'+$('#value_trans_viewed').val()+'</span>');
        }
    });
}

function updateViewAllOfComment(){
    $.ajax({
        'url' : $('#url_upviewall_comment').val(),
        'type' : 'POST',
        'dataType' : 'JSON',
        'success' : function(data){
            if(data)
                $('.view-all').html('<span style="color: #999">'+$('#value_trans_viewed').val()+'</span>');
        }
    });
}
