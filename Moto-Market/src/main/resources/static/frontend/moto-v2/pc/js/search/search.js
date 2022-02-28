$(function() {
    var redirect_timeout = null;

    var view = $("#view_type").val();
    if (view != 'grid' && view != '' && view) {
        view = "view=" + view;
    } else {
        view = '';
    }

    //Proccess Event
    var filter_form = $('#left-filters'),
        price_from = $('#filter-price-from-list'),
        price_to = $('#filter-price-to-list'),
        slider = filter_form.find('.slider-input'),
        route_bikelist = filter_form.attr('action');

    var seo_page_flag = 'SEO_PAGE' in window && window.SEO_PAGE;

    //Event filter by brand
    var filter_brand = $('#filter-brand-list');
    var select_brand = false;
    filter_brand.find('input:not(.list_filter)').change(function() {
        select_brand = true;
        createParam();
    });

    //Event filter by type
    var filter_type = $('#filter-type-list');
    var select_type = false;
    filter_type.find('input:not(.list_filter)').change(function() {
        select_type = true;
    });
    //Event filter by loc
    var filter_location = $('#filter-city-list');
    var select_location = false;
    filter_location.find('input:not(.list_filter)').change(function() {
        select_location = true;
    });

    filter_form.find('input:not(.list_filter)').change(function() {
        createParam();
    });

    filter_form.find('select').change(function() {
        createParam();
    });

    filter_form.find('#filter-price-list button').click(function() {
        price_from.data('has-value', true);
        price_to.data('has-value', true);
        createParam();
    });

    $("#product-order").change(function() {
        createParam();
    });

    $("#limit_product").change(function() {
        createParam();
    });

    $('#viewGrid').click(function() {
        view = "";
        createParam();
        return false;
    });

    $('#viewList').click(function() {
        view = "view=list";
        createParam();
        return false;
    });
    //End Proccess Event

    //Create All Param
    function createParam() {
        var queryString = $('#query_string').val(),
            url = [];

        if (!seo_page_flag) {
            //Set search fulltext
            var key_word = $('#key_word').val();
            if (key_word) {
                url.push($.param({
                    q: key_word
                }));
            }
        }

        //Set View
        if (view != '') {
            url.push(view);
        }

        //Set Seres
        var series_code = $('#series_code').val();
        if (0 != series_code) {
            series_code = 'sc=' + series_code;
            url.push(series_code);
        }

        //Brand
        var mc = getParam("#filter-brand-list input:checked", "mc");
        mc = createQueryString(queryString, 'mc', mc, select_brand);
        if ('' != mc) {
            url.push(mc);
        }

        //Types
        var type = getParam("#filter-type-list input:checked", "type");
        type = createQueryString(queryString, 'type', type, select_type);
        if ('' != type) {
            url.push(type);
        }

        //Status
        var status = getParam("#filter-status-list input:checked", "new");
        if ('' != status) {
            url.push(status);
        }

        //Capacity
        var capacity = getParam("#filter-capacity-list input:checked", "cc");
        if ('' != capacity) {
            url.push(capacity);
        }

        //Price
        if (price_from.data('has-value')) {
            var price_from_value = $("#filter-price-from-list").val().replace(/[\,]/g, "");
            if ('' != price_from_value && price_from_value != slider.attr('data-min')) {
                price_from_value = 'price_from=' + price_from_value;
                url.push(price_from_value);
            }
        }

        if (price_to.data('has-value')) {
            var price_to_value = $("#filter-price-to-list").val().replace(/[\,]/g, "");
            if ('' != price_to_value && price_to_value != slider.attr('data-max')) {
                price_to_value = 'price_to=' + price_to_value;
                url.push(price_to_value);
            }
        }

        // Tuning Status
        var tuning_status = getParam("#filter-tuning-list input:checked", "tuning");
        if ('' != tuning_status) {
            url.push(tuning_status);
        }

        //Year
        if ('' != $("#filter-year-from-list").val() && '' != $("#filter-year-to-list").val()) {
            var year_from = parseInt($("#filter-year-from-list").val());
            var year_to = parseInt($("#filter-year-to-list").val());
            if (year_from > year_to) {
                temp = year_from;
                $("#filter-year-from-list").val(year_to);
                $("#filter-year-to-list").val(temp);
            }
        }

        if ('' != $("#filter-year-from-list").val()) {
            var year_from = $("#filter-year-from-list").val();
            year_from = 'year_from=' + year_from;
            url.push(year_from);
        }
        if ('' != $("#filter-year-to-list").val()) {
            var year_to = $("#filter-year-to-list").val();
            year_to = 'year_to=' + year_to;
            url.push(year_to);
        }

        //Odo
        var odo = $("#filter-odo-ddl").val();
        if ('' != odo) {
            url.push("odo=" + odo);
        }

        //Location
        var location = getParam("#filter-city-list input:checked", "loc");
        location = createQueryString(queryString, 'loc', location, select_location);
        if ('' != location) {
            url.push(location);
        }

        //Color
        var color = getParam("#filter-color-list input:checked", "color");
        if ('' != color) {
            url.push(color);
        }

        //Order
        if ("" != $("#product-order").val()) {
            var order = $("#product-order").val();
            if(order){
                order = order.replace("_", "=");
            }
            url.push(order);
        }

        //Limit
        if ("" != $("#limit_product").val()) {
            var limit = 'limit=' + $("#limit_product").val();
            url.push(limit);
        }

        var redirect = route_bikelist;

        if (url.length > 0) {
            redirect += '?' + url.join("&");
        }

        // $.fancybox.showLoading();

        if (redirect_timeout != null) {
            clearTimeout(redirect_timeout);
        }

        redirect_timeout = setTimeout(function() {
            window.location = redirect;
        }, 500);
    }

    //Get Param
    function getParam(field, param) {
        var arg = new Array();
        $.each($(field), function(index, value) {
            arg.push($(value).val());
        })
        arg = arg.sort(function(a, b) {
            return a - b
        });
        url = '';
        if (arg.length > 0) {
            url = param + '=' + arg.join("_");
        }
        return url;
    }

    //Process query string
    function createQueryString(queryString, param, string, isSelect) {
        var arr = queryString.split("&");
        if (isSelect == false) {
            $.each($(arr), function(index, value) {
                tmp = value.split("=");
                if (tmp[0] == param && value != string) {
                    //Asign old value
                    string = value;
                }
            })
        }
        return string;
    }

    /*
    |--------------------------------------------------------------------------
    | For filtered tags sections
    |--------------------------------------------------------------------------
    | Author: Tan 2015/09/30
    */

    var checkbox_lists = $('#left-filters ul.checkbox-list'),
        range_lists = $('#filter-year-list, #filter-odo-list'),
        color_list = $('#filter-color-list'),

        filter_holder = $('.filter-tags'),
        clear_btn = filter_holder.find('a.clear').closest('li'),
        show_flag = false;
    //If not product
    if ($('#has_filter').val() == 0) {
        show_flag = true;
    }

    // create filter tags
    function addFilterTag(input, text) {
        show_flag = true;
        var item = $('<li />').html(text),
            remove = $('<a class="remove">&nbsp;</a>');

        // prepend to filter holder
        item.append(remove).insertBefore(clear_btn);

        // remove button
        remove.one('click', function() {
            input.val('').prop('checked', false).data('has-value', true);
            createParam();
            return false;
        });
    }

    // show filters if there are selected filters
    if (show_flag) {
        filter_holder.parent().slideDown().removeClass('hidden');

        // show indicator when change page
        filter_holder.find('a').on('click', function() {
            // $.fancybox.showLoading();
        })
    }
});

!(function() {
    // force target = _blank for all links in bike list
    $('.item > a, .item-row > a, .item-row a.detail', '.product-grid').attr('target', '_blank');

    // resize page h1 to fit in one line
    var title = $('.page_title h1'),
        w = title.parent('.page_title').width() - title.siblings('form').width() - 20;

    title.css('width', w + 'px');
})();
