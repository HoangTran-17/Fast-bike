/*
|--------------------------------------------------------------------------
| General scripts storing cookies for shopping
|--------------------------------------------------------------------------
*/

$(function() {

    var language_selection = $("#language_selection");

    language_selection.find("a").click(function() {
        var zenli = $(this).data('zenli');
        // set cookies zenli when click on flag language
        WVN.cookies('zenli', zenli, {
            expires: 31
        });
        WVN.cookies('zenli_tmp', zenli, {
            expires: 31
        });
    });
}(jQuery));
