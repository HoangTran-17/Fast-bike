var FOLLOW_PRODUCT  = 1;
var FOLLOW_CUSTOMER = 2;
var LIMIT_INPUT_COMMENT = $('#limit_input_comment').val();

$(function(){
	if($('.input-counter').length > 0){
		Comment.getCounterLimitComment();
	}
});
var Comment = {
		//add events to limit number of character input comment
		getCounterLimitComment:function(){
			$(document).on('keydown keyup focusout', '.input-counter', function(){
				Comment.processLimitCounterComment($(this).find('textarea'), $(this).find('.counter'), LIMIT_INPUT_COMMENT);
			});
		},
		processLimitCounterComment: function(input_object, counter_display_object, limit){
			if ($(input_object).val().length > limit) {
				//cut
				var limit_str = $(input_object).val().substring(0, limit);
				$(input_object).val(limit_str);
			} else {
				var limitCount = limit - $(input_object).val().length;
				$(counter_display_object).text(limitCount + ' / ' + LIMIT_INPUT_COMMENT);
			}
		},
		getChildComments: function(object_item_id, parent_id, show_more){
			var data	=	{
				'comment_parent_id':	parent_id,
				'show_more':	show_more,
				'object_item_id':	object_item_id
			};
			$('.reply .questions').hide();
        	$.ajax({
      		  data: data,
	          method: "GET",
	          url: $('#url_getchild_comments').val()
	        })
	        .done(function( data ) {

      		   $('.parent_comment_' + parent_id).html(data);
      		 $('.parent_comment_' + parent_id).show();
      		    var target = $('.parent_comment_' + parent_id).siblings('ul.questions');
      		    // show current
      		    target.slideDown().show()
      		        .find('>li.form textarea').focus();
	        });
		},
		getShowMoreComment(object_item_id, comment_parent_id){
			var default_offset_comment	=	parseInt($('.show-more-comment-' + comment_parent_id + ' a').attr('data-offset'));
			var limit_showmore_comment	=	parseInt($('#limit_showmore_comment').val());
			var total_parent_comment	=	parseInt($('.totalcomment-' + comment_parent_id).attr('data-total-childcomment'));
			var data	=	{
					'comment_parent_id':	comment_parent_id,
					'object_item_id':	object_item_id,
					'offset'	:	default_offset_comment
			};
	        $.ajax({
	      		  data: data,
		          method: "GET",
		          url: $('#url_showmore_comment').val()
		    })
		    .done(function(data) {
		    	if(data != '0'){
		    		$('.show-more-comment-' + comment_parent_id + ' a').attr('data-offset', limit_showmore_comment + default_offset_comment)
		    		$('.show-more-comment-' + comment_parent_id).before(data);

		    		if(total_parent_comment <= limit_showmore_comment + default_offset_comment){
		    			//remove show more
			    		$('.show-more-comment-' + comment_parent_id).remove();
		    		}
		    	}else{
		    		//remove show more
		    		$('.show-more-comment-' + comment_parent_id).remove();
		    	}
		    });
	        return false;

		},
		//post comment (parent comment or reply comment
		postComment: function(object_item_id, parent_id){
			var textarea	=	$('.comment_post_content_' + parent_id);
			var displayname	=	$('.comment-fullname-' + parent_id);
			if($(displayname).val().trim() == ''){
				WVN.alert(LANG.comment_noinput_fullname);
				return false;
			}
			if($(textarea).val().trim() == ''){
				WVN.alert(LANG.comment_noinput_content);
				return false;
			}
			if($(textarea).val().length > 200){
				WVN.alert(LANG.post_comment_maxlimit);
				return false;
			}
			$(textarea).attr("disabled","disabled");
			var data	=	{
					'comment_parent_id':	parent_id,
					'object_item_id':	object_item_id,
					'comment_content': $(textarea).val(),
					'comment_fullname': $(displayname).val(),
					'_token':	$('#form_token').val()
			};
	        $.ajax({
	      		  data: data,
		          method: "POST",
		          url: $('#url_post_comment').val()
		    })
		    .done(function(data) {
		    	//reset counter limit comment
    			$('.form-post-comment-' + parent_id).find('.counter').text(LIMIT_INPUT_COMMENT + ' / ' + LIMIT_INPUT_COMMENT);
		    	$(textarea).val('');
		    	if(data == '0'){
		    		WVN.alert(LANG.comment_noinput_content);
		    	}else{
		    		if(data != '3'){
			    		if(data == '2'){
			    			WVN.alert(LANG.comment_noinput_content);
			    		}else{

					    	if(parent_id > 0){
					    		$('.form-post-comment-' + parent_id).before(data);
					    	}else{
					    		$('.form-post-comment-' + parent_id).after(data);
					    	}
					    	$(textarea).removeAttr("disabled");
			    		}
		    		}else{
		    			WVN.alert(LANG.comment_nologin);
		    		}
		    	}

		    });
	        return false;
		},
		//send request follow product
		followProduct: function(obj){
			var data	=	{
				'product_id':	$(obj).attr('data-id'),
				'data_type' :   $(obj).attr('data-type'),
				'_token'	:	$('#form_token').val()
			};
	        $.ajax({
	        	 data: data,
		         method: "POST",
		         url: $('#url_follow_product').val()
		   })
		   .done(function( data ) {
		   		//set message for product or for shop
		   		var follow_success = $(obj).attr('data-type') == FOLLOW_CUSTOMER ? LANG.follow_shop_successfully: LANG.follow_product_successfully;
		   		var follow_nosuccess = $(obj).attr('data-type') == FOLLOW_CUSTOMER ? LANG.follow_shop_nosuccessfully: LANG.follow_product_nosuccessfully;
		   		var follow_nologin = $(obj).attr('data-type') == FOLLOW_CUSTOMER ? LANG.follow_shop_nologin: LANG.follow_nologin;
			   if(data != '2'){
				   if(data == '1'){
					   WVN.alert(follow_success); //Needn't show msg
					   $(obj).addClass('active');
					   $('.btn.follow').each(function() {
						   $( this ).addClass('active');
					   });

				   }else{
				   	   $(obj).blur();
					   $(obj).removeClass('active');
					    WVN.alert(follow_nosuccess);
				   }
			   }else{
			   		$(obj).blur();
				    WVN.alert(follow_nologin);
			        $(obj).removeClass('active');

			   }
		   });
		}
};