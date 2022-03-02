(function ($) {
    "use strict";


    /*==================================================================
    [ Focus input ]*/
    $('.input100').each(function () {
        $(this).on('blur', function () {
            if ($(this).val().trim() != "") {
                $(this).addClass('has-val');
            } else {
                $(this).removeClass('has-val');
            }
        })
    })


    /*==================================================================
    [ Validate ]*/
    var input = $('.validate-input .input100');

    $('.validate-form').on('submit', function () {
        var check = true;
        for (var i = 0; i < input.length; i++) {
            if (validate(input[i]) == false) {
                showValidate(input[i]);
                check = false;
            }
        }
        if (check) {
            event.preventDefault();
            // let email =  $("#email-input").val();
            let email = $("input[type = 'email']").val();
            $("#exampleModalLongTitle").text("Verify Your Email: " + email)
            $('#verifyEmailModal').modal('show');
            $.ajax({
                type: "GET",
                headers: {
                    "Content-Type": "text/html",
                    "Accept": "text/html"
                },
                url: "/api/verify-mail/" + email,
            }).done(function (data) {
            }).fail(function () {
                alert("error")
            })
        }
        return check;
    });

    $("#verify-btn").on("click", function () {
       let code = $("#code-input").val();
        $.ajax({
            type: "GET",
            headers: {
                "Content-Type": "text/html",
                "Accept": "text/html"
            },
            url: "/api/verify-code/" + code,
        }).done(function (data) {
            if (data === "true"){
                $('#verifyEmailModal').modal('hide');
                // $.Toast('Mã PIN đúng, đăng kí thành công, hãy đăng nhập để tiếp tục', {'position': 'top','class': 'success', 'duration': 3000});
                $("#register-form").submit();
            } else {
                $.Toast("Mã PIN không đúng, hãy kiểm tra lại!!", {'position': 'top','class': 'alert', 'duration': 1500});
            }
        }).fail(function () {
            alert("error")
        })
    });


    $('.validate-form .input100').each(function () {
        $(this).focus(function () {
            hideValidate(this);
        });
    });

    function validate(input) {
        if ($(input).attr('type') == 'email' || $(input).attr('name') == 'email') {
            if ($(input).val().trim().match(/^([a-zA-Z0-9_\-\.]+)@((\[[0-9]{1,3}\.[0-9]{1,3}\.[0-9]{1,3}\.)|(([a-zA-Z0-9\-]+\.)+))([a-zA-Z]{1,5}|[0-9]{1,3})(\]?)$/) == null) {
                return false;
            }
        } else {
            if ($(input).attr('name') == 'phoneNumber') {
                if ($(input).val().trim().match(/^(0?)(3[2-9]|5[6|8|9]|7[0|6-9]|8[0-6|8|9]|9[0-4|6-9])[0-9]{7}$/) == null) {
                    return false;
                }
            } else {
                if ($(input).val().trim() == '') {
                    return false;
                }
            }
        }
    }

    function showValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).addClass('alert-validate');
    }

    function hideValidate(input) {
        var thisAlert = $(input).parent();

        $(thisAlert).removeClass('alert-validate');
    }

    /*==================================================================
    [ Show pass ]*/
    var showPass = 0;
    $('.btn-show-pass').on('click', function () {
        if (showPass == 0) {
            $(this).next('input').attr('type', 'text');
            $(this).find('i').removeClass('zmdi-eye');
            $(this).find('i').addClass('zmdi-eye-off');
            showPass = 1;
        } else {
            $(this).next('input').attr('type', 'password');
            $(this).find('i').addClass('zmdi-eye');
            $(this).find('i').removeClass('zmdi-eye-off');
            showPass = 0;
        }

    });


})(jQuery);