$(function() {

    /*
    |--------------------------------------------------------------------------
    | Init form
    |--------------------------------------------------------------------------
    */

    var form = $('#form-edit-profile'),
        email = form.find('input[name="email"]'),
        passwd = form.find('input[name="passwd"]'),
        phone = form.find('input[name="add_tel"]'),
        btn_submit = form.find('button[type="submit"]'),
        errors = form.find('#error_profile'),

        // GLOBAL API: province ~ state
        province = form.find('select[name="province_code"]'),
        add_state = form.find('input[name="add_state"]'),

        // GLOBAL API: district ~ city
        district = form.find('select[name="district_code"]'),
        add_city = form.find('input[name="add_city"]'),

        flag_can_submit = false,
        
        flag_mail_error = true,
        flag_phone_error = true,
        flag_phone_verify = false;

        xhr = null,
        xhr_callback = null;


    //if personal will load district
    if (!district.prop("disabled")) {
        //district.prop('disabled', true);
        // load default data for district
        loadDistrict(district, province.val(), district.val());
    }

    // focus on the first visible input
    form.find('input:visible:first').focus();


    if (email.prop('disabled') && phone.prop('disabled')) {
        flag_mail_error = false;
        flag_can_submit = true;
    }

    form.submit(function(event) {
        
        if (!form.valid() || flag_mail_error || flag_phone_error) {
            if (flag_mail_error) {email.focus();}
            if (flag_phone_error) {phone.focus();}
            
            return true;
        }

        if (!flag_can_submit && !phone.prop('disabled') && flag_phone_verify==false) {
            verifyPhone();
            return false;
        }else{
            return true;
        }
        
        // submit form if everything ok
        if (flag_can_submit == true) {
            disableForm(true);
            return true;
        }
        return false;
    });

    setTimeout(function() {
        checkUnique();
    }, 1000);

    /*
    |--------------------------------------------------------------------------
    | Input event listeners
    |--------------------------------------------------------------------------
    */

    province.change(function() {
        loadDistrict(district, $(this).val(), null);

        // GLOBAL API using province name and district name -> set value for add_state, add_city
        add_state.val($.trim(province.children(':selected').text()));

        province.trigger('blur');
    });

    district.change(function() {
        // GLOBAL API using province name and district name -> set value for add_state, add_city
        add_city.val($.trim(district.children(':selected').text()));

        district.trigger('blur');
    });

    email.on('change', function() {
        btn_submit.prop('disabled', true);
        flag_mail_error = true;
        if (email.valid()) {
            checkUnique();
        }
    });

    phone.on('change', function() {
        var numberPhone = $(this).val();
        if(numberPhone == ''){
            flag_phone_error = true; 
        }else{
            flag_phone_error = false;    
        }
    });

    passwd.on('change', function() {
        btn_submit.prop('disabled', false);
        if (passwd.valid()) {
            // checkUnique();
            return true;
        }
    });

    /*
    |--------------------------------------------------------------------------
    | Helper functions
    |--------------------------------------------------------------------------
    */
    function checkUnique() {
        errors.hide();
        setTimeout(function() {
            if (xhr) {
                xhr.abort();
            }
            var form_data = form.serialize();
            xhr = $.ajax({
                type: 'post',
                url: WVN.routes['ajax_check_unique_phone_or_email'],
                data: form_data + '&ajax_check_unique_phone_or_email=true&ajax_check_password=false',
                dataType: 'json',
                success: function(data) {
                    if (data.error_messages) {
                        appendErrors(data.error_messages);
                        return;
                    }

                    if (data.verified == true) {
                        flag_mail_error = false;
                        flag_can_submit = true;
                        form.submit();
                    } else {
                        flag_mail_error = false;
                    }
                },
                error: function(jqXHR) {
                    if (jqXHR.status == 422) {
                        appendErrors(jqXHR.responseJSON);
                    }
                }
            });
            disableForm(false);
            btn_submit.prop('disabled', false);
        }, 500);
    }

    function resetForm(target, state) {
        if (state) {
            $(target).show().find('select, input').prop('disabled', false);
        } else {
            $(target).hide().find('select, input').prop('disabled', true);
        }
    }

    function disableForm(bool) {
        if (bool) {
            form.addClass('disabled');
        } else {
            form.removeClass('disabled');
        }
    }

    function loadDistrict(target, province_code, old_district_code) {
        target.find('option').remove();
        target.trigger('change').prop('disabled', false);

        target.append('<option value="">' + district_placeholder + '</option>')
        for (var i = 0; i < districts.length; i++) {
            // append district depend on province
            if (province_code == districts[i].province_code) {
                target.append('<option value="' + districts[i].district_code + '">' + districts[i].district_name + '</option>')
            }
        };

        if (old_district_code) {
            target.val(old_district_code).trigger('change');
        }

        var parent_tag = target.closest('button');
        if (parent_tag.prop("tagName") == 'BUTTON') {
            if (parent_tag.children().first().text() == ' ')
                parent_tag.children().first().text(district_placeholder);
        }
    }

    function appendErrors(json) {
        var msg = '<ul>';
        for (var field in json) {
            if (field != 'email') {
                flag_mail_error = false;
            }
            if ($.isArray(errors[field])) {
                msg += '<li>' + json[field][0] + '</li>';
            } else {
                msg += '<li>' + json[field] + '</li>';
            }
        };
        msg += '</ul>';

        errors.html(msg).show();
    }

    //handle preview photo when upload
    //*
    $("#fileUpload").on('change', function() {
        if (this.value) {
            $("#error_image_upload").html('');
            if (typeof(FileReader) != "undefined") {

                //remove class broken if have
                if ($("#avatar img").hasClass('broken')) {
                    $("#avatar img").removeClass('broken');
                }

                var image_holder = $("#img_holder");
                image_holder.empty();

                var ext = this.value.match(/\.(.+)$/)[1];

                switch (ext) {
                    case 'jpg':
                    case 'jpeg':
                    case 'png':
                        break;
                    default:
                        $("#error_image_upload").append(error_image_upload_message);
                        $("#error_image_upload").show();
                        image_holder.attr("src", $("#src_customer_avatar").val());
                        image_holder.show();
                        this.value = '';
                        return false;
                }

                $("#error_image_upload").html('');
                $("#error_image_upload").hide();

                var reader = new FileReader();
                reader.onload = function(e) {
                    image_holder.attr("src", e.target.result);
                }
                image_holder.show();
                reader.readAsDataURL($(this)[0].files[0]);
            } else {
                alert("This browser does not support FileReader.");
            }
        }
    });

    function callback(response) {
        if (response.status === "PARTIALLY_AUTHENTICATED") {
            if (xhr_callback) {
                xhr_callback.abort();
            }
            xhr_callback = $.ajax({
                type: 'post',
                url: WVN.routes['ajax_ack'],
                data: {
                    code: response.code,
                    state: response.state,
                    email: email.val(),
                },
                dataType: 'json',
                success: function(data) {
                    if (data.error_messages) {
                        appendErrors(data.error_messages);
                        return;
                    }

                    flag_can_submit = true;
                    form.submit();
                },
            });
        } else if (response.status === "NOT_AUTHENTICATED") {
            // handle authentication failure
        } else if (response.status === "BAD_PARAMS") {
            // handle bad parameters
        }
        return false;
    }

    function verifyEmail(emailAddress) {
        AccountKit.login('EMAIL', { emailAddress: emailAddress }, callback);
    }
    
    function callbackPhone(response) {
        if (response.status === "PARTIALLY_AUTHENTICATED") {
            if (xhr_callback) {
                xhr_callback.abort();
            }
            xhr_callback = $.ajax({
                type: 'post',
                url: WVN.routes['ajax_ack'],
                data: {
                    code: response.code,
                    state: response.state,
                    phone: phone.val(),
                },
                dataType: 'json',
                success: function(data) {
                    if (data.error_messages) {
                        appendErrors(data.error_messages);
                        return;
                    }

                    flag_can_submit = true;
                    form.submit();
                },
            });
        } else if (response.status === "NOT_AUTHENTICATED") {
            // handle authentication failure
        } else if (response.status === "BAD_PARAMS") {
            // handle bad parameters
        }
        return false;
    }
    
    function verifyPhone() {
        let otherWindow = window.open($('#verify-phone').val(), 'Verify SMS', 'width=500,height=600,top=20,right=20');
        // $("#layout").show();
        let timer = setInterval(function () {
            if (otherWindow.closed) {
            $("#layout").hide();
                clearInterval(timer);
                if ($('#access_token').val()) {
                    flag_can_signup = true;
                    flag_phone_verify =true;
                    form.submit();
                }

            }
        }, 1000);
    }
});
