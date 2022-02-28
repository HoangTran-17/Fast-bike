$(function() {

    // init modules
    var recommendation = MODULES.initProductGridModules();

    /*
    |--------------------------------------------------------------------------
    | Preview thumbnail
    |--------------------------------------------------------------------------
    */

    var MAX_THUMBNAILS = 5;
    var THUMBNAIL_WIDTH = 60;
    var THUMBNAIL_HEIGHT = 45;

    var preview = $('#preview'),
        preview_img = preview.find('> img'),
        thumbnails = $('#thumbnails'),
        thumbnails_li = thumbnails.find('li'),
        thumbnails_a = thumbnails.find('a');
    // thumbnails_last_visible = thumbnails_li.eq(MAX_THUMBNAILS - 1),
    // thumbnails_hidden = thumbnails_last_visible.nextAll('li');

    var button_show_phone = $('a.btn-show-address');
    var popup_show_phone = $('#popup_show_phone');

    var right_bar = $('#right');

    // pin right-bar when scrolling
    if ('fixTo' in right_bar) {
        right_bar.fixTo('#wrapper', {
            top: 10,
            useNativeSticky: WVN.isMobile || false
        });
    }

    // hide preloader when loaded
    preview_img.on('load error', function(e) {
        preview.find('.loading').hide();

        if (e.type == 'error' && preview.attr('src') != '') {
            preview.attr('src', '');
        }
    });

    // collapse thumbnails
    // if (thumbnails_hidden.length) {
    //     // force lazyload for hidden thumbnails
    //     thumbnails_hidden.hide().find('img').trigger('unveil');

    //     // add counting number for the last thumbnail
    //     var more = thumbnails_hidden.length < 10 ? '+' + thumbnails_hidden.length : thumbnails_hidden.length + '+';
    //     thumbnails_last_visible.find('figure').append('<span class="more">' + more + '</span>');
    // } else {
    //     thumbnails_last_visible = null;
    // }

    // init fancy box for photos
    if ('fancybox' in thumbnails_a) {
        var fancy_options = {
            padding: 10,
            margin: 50,
            prevEffect: 'none',
            nextEffect: 'none',

            type: 'image',

            closeBtn: true,
            arrows: false,
            nextClick: true,
            scrolling: 'no',

            helpers: {
                thumbs: {
                    width: THUMBNAIL_WIDTH,
                    height: THUMBNAIL_HEIGHT
                },
                overlay: {
                    locked: true
                }
            }
        };

        var fancy_attributes = {
            'rel': 'product-review',
            'data-fancybox-group': 'product-review'
        };

        thumbnails_a.attr(fancy_attributes).fancybox(fancy_options);

        // trigger fancy box for preview button
        preview.find('.expand').click(function() {
            thumbnails_a.eq($(this).data('target') || 0).trigger('click.fb-start');
            return false;
        });
    }

    // trigger change preview img for the rest of thumbnails
    var preview_thumbnails = thumbnails_a;

    // exclude the last visible thumbnail if it presented
    // if (thumbnails_last_visible) {
    //     preview_thumbnails = thumbnails_a.not(thumbnails_last_visible.find('a'))
    // }

    // for preview thumbnails
    preview_thumbnails.click(function() {
        preview_img.removeAttr('src');
        preview.find('.expand').data('target', preview_thumbnails.index(this));
        preview.find('.loading').stop().fadeIn().show();
        preview_img.attr('src', $(this).data('preview') || this.href).removeClass('broken');

        thumbnails_a.removeClass('active').filter(this).addClass('active');
        return false;
    });

    thumbnails_a.first().click();

    /*
    |--------------------------------------------------------------------------
    | Questions & Replies
    |--------------------------------------------------------------------------
    | These code for questions and replies are deprecated.
    | Developers should implement later
    */

    // $('a.reply-btn').click(function() {
    //     var target = $(this).siblings('ul.questions');

    //     // hide opening replies
    //     // $('ul.questions.replies').not(target).hide();

    //     // show current
    //     target.slideDown().show()
    //         .find('>li.form textarea').focus();

    //     return false;
    // });

    /*
    |--------------------------------------------------------------------------
    | Reveal address
    |--------------------------------------------------------------------------
    | These code for questions and replies are deprecated.
    | Developers should implement later
    */

    var revealed_top = false;
    var revealed_bottom = false;

    $('#right-shop-info a.btn-show-address').click(function() {
        $(this).hide().siblings('address').fadeIn('fast').addClass('revealed');
        revealed_top = true;
        return false;
    })

    $('.fixed-bottom a.btn-show-address').click(function() {
        $(this).hide().siblings('address').fadeIn('fast').addClass('revealed');
        revealed_bottom = true;
        resizeButtonCall();
        return false;
    })

    $('#right-shop-info a.btn-show-address').click(function() {
        resizeButtonCall();
        return false;
    })

    // Event for popup show phone - 20160928: pending event
    /*
    if (popup_show_phone.length){
        // open review popup
        if ('fancybox' in button_show_phone) {
            button_show_phone.fancybox({
                padding: 0,
                modal: true,
                wrapCSS: 'modal',
                width: 400,
                autoSize: false,
                autoHeight: true
            });
        } else {
            button_show_phone.click(function() {
                return false;
            });
        }
    }
    */
    // End Show popup

    function resizeButtonCall() {
        if ($('.show-phone-top').width() > 240) {
            var width = $('#right-shop-info table').width() - $('.show-phone-top').width() - 60;
            $('#right-shop-info .btn.follow').css('width', width + 'px');
            $('#right-shop-info .btn.follow').parent().css('width', width + 'px');
        }
    }

    //Set zoomLens image
    $("#img_01").elevateZoom({
        gallery: 'thumbnails',
        cursor: 'pointer',
        galleryActiveClass: 'active',
        imageCrossfade: true
    });

    //Event zoom
    $("#img_01").bind("click touchstart", function(e) {
        var ez = $("#img_01").data('elevateZoom');
        $.fancybox(ez.getGalleryList());
        return false;
    });

    //Check device of Apple
    if ((/ip(hone|ad|od)/i).test(navigator.userAgent)) {
        setTimeout(function() {
            $("#img_01").data('elevateZoom').zoomContainer.css('pointer-events', 'none');
        }, 1000);
    }

    //Event Tab
    var tabs = $('.tabs');
    tabs.each(function() {
        var self = $(this),
            tab_links = self.find('.tab_links');
        tab_links.find('.item:first > a').click();
        self.on('click', '.tab_links a', function() {
            var link = $(this),
                target = self.find(link.attr('href'));
            if (target.length) {
                target.addClass('show').trigger('shown');
                target.siblings().removeClass('show').trigger('hidden');
            }

            self.find('.item').removeClass('selected');
            link.closest('.item').addClass('selected');
            return false;
        });
    });

    //Event scoll to show div bottom
    var bottom_phone = $('#bottom-phone');
    if (bottom_phone.length && !($('.expired-tag').length || $('.sold-tag').length)) {
        var shown_flag = true;
        $(window).scroll(function() {
            var scrolltoppx = $(this).scrollTop();
            if (scrolltoppx > 550 && !shown_flag) {
                if (!WVN.cookies.get('close_footer_banner')) {
                    $('.footer_banners').css('left', $('#content').position().left);
                    $('.footer_banners').show();
                }
                bottom_phone.removeClass('hidden');
                $('footer').css('padding-bottom', '45px');

                if (revealed_top && !revealed_bottom) {
                    $('.btn-show-address').hide();
                    $('.show-phone-bottom').addClass('revealed');
                }

                if (!revealed_top && revealed_bottom) {
                    $('.show-address').hide();
                    $('.show-phone-top').addClass('revealed');
                }
            } else {
                $('.footer_banners').hide();
                shown_flag = false;
                bottom_phone.addClass('hidden');
            }
        }).scroll();
    }

    setTimeout(function() {
        $(window).trigger('scroll');
    }, 200);

    //Event scroll thumnail
    $('#thumbnails').slick({
        infinite: false,
        slidesToShow: 4,
        slidesToScroll: 4
    });

    //Event scroll block media
    if ($('.media_feed').length && $('.col_left').length) {
        $('.media_feed').fixTo('.col_left', {
            top: 10,
            useNativeSticky: WVN.isMobile || false
        });
    }


    //close footer banner on bike-detail
    $('.footer_banners span a').click(function() {
        $('.footer_banners').hide();
        WVN.cookies('close_footer_banner', true, {
            expires: 1
        });
    });

    /*
    |--------------------------------------------------------------------------
    | Re-up product
    |--------------------------------------------------------------------------
    */
    var xhr_reup = null;
    $('a.reup').click(function() {
        if (xhr_reup) {
            xhr_reup.abort();
        }
        var pid = $(this).data('pid');

        xhr_reup = $.ajax({
            method: "GET",
            url: WVN.url('ajax_product_reup_get'),
            data: { pid: pid },
        }).done(function(data) {
            if (data.can_reup == true) {
                WVN.confirm(data.msg, function(result) {
                    if (result) {
                        $.ajax({
                            method: "POST",
                            url: WVN.url('ajax_product_reup_post'),
                            data: { pid: pid },
                        }).done(function(data) {
                            WVN.alert(data.msg);
                        });
                    }
                });
            } else {
                WVN.alert(data.msg);
            }
        });
    });

    /*
    |--------------------------------------------------------------------------
    | Report product
    |--------------------------------------------------------------------------
    */
    //Show popup
    var report_product = $('a.report-product');
    report_product_form = report_product.find('form');
    if (report_product.length) {
        // open report popup
        if ('fancybox' in report_product) {
            report_product.fancybox({
                padding: 0,
                modal: true,
                wrapCSS: 'modal',
                width: 330,
                autoSize: false,
                autoHeight: true
            });
        } else {
            report_product.click(function() {
                return false;
            });
        }
    }

    //Event click report product
    var xhr_report_product = null;
    var btn_report_product = $('a.btn-report-product');
    var other_report = $('textarea[name="other_report"]');
    btn_report_product.on('click', function() {
        if (xhr_report_product) {
            xhr_report_product.abort();
        }

        var pid = $(this).data('pid');
        report_id = 0;
        $('#report-product').find('input:checked').each(function() {
            report_id = $(this).val();
        });

        report_content = '';
        if (report_id == 1) {
            report_content = other_report.val();
            if (report_content.length > 200) {
                WVN.alert(WVN.trans('report_product_long_content'));
                return;
            }
        }

        if (report_id == 0) {
            WVN.alert(WVN.trans('report_product_please_choose'));
            return;
        }
        xhr_report_product = $.ajax({
            method: "POST",
            url: WVN.url('ajax_report_product_post'),
            data: { pid: pid, report_id: report_id, report_content: report_content, '_token': $('#form_token').val() },
        }).done(function(data) {
            WVN.alert(data.msg);
            reset_report_form();
            $.fancybox.close();
        });
    });

    // Event single select check box
    $('#report-product').find('input:checkbox').on('click', function() {
        var $box = $(this);
        if ($box.is(":checked")) {
            report_id = $box.val();
            if ($box.val() == 1) {
                other_report.attr('disabled', false);
            } else {
                other_report.attr('disabled', true);
            }
            var group = "input:checkbox[name='" + $box.attr("name") + "']";
            $(group).prop("checked", false);
            $box.prop("checked", true);
        } else {
            $box.prop("checked", false);
        }
    });

    function reset_report_form() {
        $('#report-product').find('input:checked').attr('checked', false);
        other_report.val('');
    }

    /*
    |--------------------------------------------------------------------------
    | Tracking view contact
    |--------------------------------------------------------------------------
    */
    var product_tracking = false;
    button_show_phone.click(function() {
        if (product_tracking == false) {
            $.ajax({
                method: "GET",
                url: url_product_tracking,
                data: { contact: true },
            }).done(function(data) {});
            product_tracking = true;
        }
    });
});
