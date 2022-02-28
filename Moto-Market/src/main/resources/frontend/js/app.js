! function() {
    for (var e, t = function() {}, i = ["assert", "clear", "count", "debug", "dir", "dirxml", "error", "exception", "group", "groupCollapsed", "groupEnd", "info", "log", "markTimeline", "profile", "profileEnd", "table", "time", "timeEnd", "timeline", "timelineEnd", "timeStamp", "trace", "warn"], n = i.length, a = window.console = window.console || {}; n--;) e = i[n], a[e] || (a[e] = t);
    for (var o = document.forms, r = o.length; r--;) o[r].setAttribute("novalidate", !0);
    var s, l = document.body;
    window.addEventListener("scroll", function() {
        clearTimeout(s), l.classList.contains("no-click") || l.classList.add("no-click"), s = setTimeout(function() {
            l.classList.remove("no-click")
        }, 50)
    }, !1)
}(), $(function() {
        if ("Cookies" in window && (WVN.cookies = Cookies.noConflict(), WVN.cookies.json = !0, WVN.cookies.path = "/"), "alertify" in window && (alertify.set({
                labels: {
                    ok: "OK",
                    cancel: "Cancel"
                },
                delay: 5e3,
                buttonReverse: !!(navigator.platform.indexOf("Win") > -1),
                buttonFocus: "ok"
            }), WVN.alert = alertify.alert, WVN.confirm = alertify.confirm, WVN.prompt = alertify.prompt, WVN.notify = alertify.log), window.init_google_maps = function() {
                var e = WVN.createMap;
                WVN.createMap = function(t, i, n, a, o) {
                    function r(e) {
                        var r = {
                                zoom: 15,
                                panControl: !1,
                                mapTypeId: google.maps.MapTypeId.ROADMAP,
                                center: e
                            },
                            s = $(t).empty(),
                            l = $("<div />").css({
                                width: (a || 198) + "px",
                                height: (o || 245) + "px",
                                display: "block"
                            }).appendTo(s),
                            c = new google.maps.Map(l.get(0), r),
                            d = new google.maps.Marker({
                                draggable: !1,
                                position: e,
                                map: c
                            });
                        return d.addListener("click", function() {
                            window.open("https://www.google.com/maps/place/" + encodeURIComponent(i))
                        }), l.attr("title", n)
                    }
                    if ($.isArray(i) && 2 == i.length) {
                        var s = new google.maps.LatLng(i[0], i[1]);
                        return r(s)
                    }
                    var l = new google.maps.Geocoder,
                        c = {
                            address: i
                        };
                    l.geocode(c, function(s, l) {
                        if (l == google.maps.GeocoderStatus.OK) {
                            var c = s.shift().geometry.location;
                            return r(c)
                        }
                        return e(t, i, n, a, o)
                    })
                }, $(window).trigger("mapsready")
            }, WVN.scrollTo = function(e, t, i) {
                e = $(e ? e : "html");
                var t = t || 400,
                    n = e.offset().top;
                return $("body, html").animate({
                    scrollTop: n - (i || 0)
                }, t, "swing"), e
            }, "select2" in $.fn && (WVN.select2 = function(e, t, i) {
                var n = $(e),
                    a = {
                        language: WVN.language,
                        matcher: function(e, t) {
                            if ("" === $.trim(e.term)) return t;
                            if (t.children && t.children.length > 0) {
                                for (var i = $.extend(!0, {}, t), n = arguments.callee, a = t.children.length - 1; a >= 0; a--) {
                                    var o = t.children[a],
                                        r = n(e, o);
                                    null == r && i.children.splice(a, 1)
                                }
                                return i.children.length > 0 ? i : n(e, i)
                            }
                            var s = WVN.helpers.remove_accents(t.text).replace(/\W+/g, ""),
                                l = WVN.helpers.remove_accents(e.term).replace(/\W+/g, "");
                            return s.indexOf(l) > -1 ? t : null
                        }
                    };
                return "object" == typeof t ? $.extend(a, t) : "string" == typeof t && (a[t] = i), n.each(function() {
                    var e = $(this),
                        t = $.extend({}, a);
                    e.is("[data-hide-search]") && (t.minimumResultsForSearch = 1 / 0), e.attr("data-placeholder", e.attr("placeholder")).select2(t)
                })
            }), "raty" in $.fn && ($.fn.raty.defaults.path = BASE + "/frontend/img/vendor", $.fn.raty.defaults.readOnly = !0, $.fn.raty.defaults.cancel = !1, $.fn.raty.defaults.space = !1, $.fn.raty.defaults.half = !0, $.fn.raty.defaults.hints = [1, 2, 3, 4, 5], WVN.ratings = function(e, t, i) {
                return $(e).each(function() {
                    var e = $(this),
                        n = parseFloat(e.data("ratings"), 10) || 0,
                        a = $.extend({}, t);
                    a.score = n, i || e.is("[data-hide-label]") || e.html('<b class="highlight">' + n.toFixed(2) + "</b> "), e.raty(a)
                })
            }), "dotdotdot" in $.fn) {
            var e = {
                ellipsis: "... "
            };
            WVN.truncate = function(t, i) {
                var n = $.extend({}, e, i);
                return $(t).dotdotdot(n).css("overflow", ""), t
            }
        }
        if ("jstz" in window && (WVN.timezone = jstz.determine().name()), "validator" in $) {
            var t = $.validator;
            t.setDefaults({
                errorElement: "p",
                errorClass: "message form-error",
                validClass: "message form-valid",
                ignore: ":hidden, [formnovalidate], [disabled]",
                onfocusout: !1,
                onkeyup: !1,
                onclick: !1,
                errorPlacement: function(e, t) {
                    var t = $(t),
                        i = t.closest(".form-group");
                    (!i.length || i.find("input:not(:radio),textarea,select").length > 1) && (i = t.parent()), i.length ? i.append(e) : e.insertAfter(t)
                },
                highlight: function(e, t, i) {
                    var e = $(e),
                        n = e.closest(".form-group");
                    (!n.length || n.find("input:not(:radio),select,textarea").length > 1) && (n = e.parent()), n.addClass(t).removeClass(i)
                },
                unhighlight: function(e, t, i) {
                    var e = $(e),
                        n = e.closest(".form-group");
                    (!n.length || n.find("input:not(:radio),select,textarea").length > 1) && (n = e.parent()), n.removeClass(t).addClass(i)
                }
            })
        }
        "trumbowyg" in $.fn && (WVN.createEditor = function(e, t) {
            return $(e).each(function() {
                var e = $(this),
                    i = $.extend(!0, {
                        fullscreenable: !0,
                        autogrow: !0,
                        removeformatPasted: !0,
                        resetCss: !0,
                        mobile: !0,
                        tablet: !0,
                        btns: ["bold", "italic", "underline", "|", "viewHTML"]
                    }, t);
                e.trumbowyg(i)
            })
        })
    }),
    function(e) {
        var t = 6e3;
        window.MODULES = {
            initTopSlider: function() {
                var i = e("#top-slider-container");
                return i.length && "slick" in i && i.slick({
                    accessibility: !1,
                    arrows: !0,
                    autoplay: !0,
                    autoplaySpeed: t,
                    dots: !0,
                    speed: 500,
                    touchThreshold: 100
                }), i
            },
            initTopVNSlider: function() {
                var i = e("#slider_topvn");
                return i.length && "slick" in i && i.slick({
                    accessibility: !1,
                    arrows: !1,
                    autoplay: !0,
                    autoplaySpeed: t,
                    dots: !0,
                    speed: 500,
                    touchThreshold: 100
                }), i
            },
            initBrandSlider: function() {
                var i = e("#brand-slider");
                return i.length && "slick" in i && i.slick({
                    accessibility: !1,
                    arrows: !0,
                    autoplay: !1,
                    autoplaySpeed: t,
                    speed: 500,
                    touchThreshold: 100
                }), i
            },
            initTopMotorBanners: function() {
                var i = e("#top-motor-banners-container");
                return i.length && "slick" in i && i.slick({
                    accessibility: !1,
                    arrows: !1,
                    autoplay: !0,
                    autoplaySpeed: t,
                    speed: 500,
                    touchThreshold: 100
                }), i
            },
            initTopMotorMakers: function() {
                var i = e("#top-motor-makers-container");
                return i.length && "slick" in i && i.slick({
                    accessibility: !1,
                    autoplay: !0,
                    autoplaySpeed: t,
                    slidesToShow: 7,
                    slidesToScroll: 7,
                    speed: 500,
                    touchThreshold: 100
                }), i
            },
            initTopMotorTypes: function() {
                var t = e("#top-motor-types-container");
                return t.length && "slick" in t && t.slick({
                    accessibility: !1,
                    slidesToShow: 5,
                    slidesToScroll: 5,
                    speed: 500
                }), t
            },
            initFeaturedModules: function() {
                var t = e(".module.featured .slider");
                return t.length && "slick" in t && (t.slick({
                    accessibility: !1,
                    draggable: !1,
                    speed: 500
                }), WVN.truncate(t.find("label"), {
                    after: ".badge"
                })), t
            },
            initBottomMotorMakers: function() {
                var i = e("#bottom-motor-makers-container");
                return i.length && "slick" in i && i.slick({
                    accessibility: !1,
                    arrows: !1,
                    autoplay: !0,
                    autoplaySpeed: t,
                    slidesToShow: 8,
                    slidesToScroll: 8,
                    speed: 800,
                    touchThreshold: 100
                }), i
            },
            initRecentlyViewedItemsModule: function() {
                var t = e("#recently-viewed-items"),
                    i = e("#recently-viewed-items-container"),
                    n = t.find(".module_footer"),
                    a = n.find(".module-arrow.next"),
                    o = n.find(".module-arrow.prev"),
                    r = i.find(".page");
                return i.length && (r.length > 1 && "slick" in i ? (i.slick({
                    accessibility: !1,
                    arrows: !1,
                    draggable: !1,
                    slidesToShow: 1,
                    slidesToScroll: 1,
                    speed: 500
                }), a.click(function() {
                    return i.slick("slickNext"), !1
                }), o.click(function() {
                    return i.slick("slickPrev"), !1
                })) : n.slideUp(), WVN.truncate(i.find("label"))), i
            },
            initProductGridModules: function() {
                var t = e(".module .product-grid.slider");
                return t.length && "slick" in t && (t.slick({
                    accessibility: !1,
                    draggable: !1,
                    speed: 500
                }), WVN.truncate(t.find("label"))), t
            },
            initBodyMediaFeedModule: function() {
                var t = e("#body-media-feed");
                t.length && "slick" in t && WVN.truncate(t.find(".sub h4"));
                var i = 36;
                return t.find(".sub .block").each(function() {
                    e(this).find("h5").each(function() {
                        i = Math.max(i, e(this).height())
                    }).css("height", i), i = 36
                }), t
            },
            initQuickContactModule: function() {
                function t(e) {
                    e = e || WVN.trans("quick_contact_msg_invalid_phone"), WVN.alert(e, function() {
                        i.find("input, button").prop("disabled", !1), i.removeClass("disabled"), n.focus()
                    })
                }
                var i = e("#quick-contact form"),
                    n = i.find('input[name="phone_number"]');
                return i.length && ("" == n.val() && n.val(WVN.cookies.get("quick-contact-number")), i.submit(function() {
                    var a = i.serialize();
                    if (i.find("input, button").prop("disabled", !0), i.addClass("disabled"), "" != n.val()) {
                        var o = e.post(WVN.url("ajax_quick_contact"), a, function(e) {
                            "id" in e && e.id && (WVN.alert(WVN.trans("quick_contact_msg_success")), WVN.cookies.set("quick-contact-number", n.val()))
                        }, "json");
                        o.fail(function(e, i, n) {
                            422 == e.status && t()
                        })
                    } else t();
                    return !1
                })), i
            },
            startLoading: function() {
                e(".spinner").css("display", "block")
            },
            stopLoading: function() {
                e(".spinner").css("display", "none")
            }
        }
    }(jQuery), ! function() {
        var e = ($("ul.top-menu").superfish({
            delay: !1,
            animation: {
                opacity: "show"
            },
            animationOut: {
                opacity: "hide"
            },
            pathLevels: 2,
            speed: "fast",
            speedOut: 100,
            cssArrows: !1,
            popUpSelector: "ul:not(.popup),.sf-mega"
        }), $("ul.menu-motor-nav").superfish({
            delay: !1,
            animation: {
                opacity: "show"
            },
            animationOut: {
                opacity: "hide"
            },
            pathLevels: 2,
            speed: "fast",
            speedOut: 100,
            cssArrows: !1,
            popUpSelector: "ul:not(.popup),.sf-mega"
        }), "data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7");
        document.body.addEventListener("error", function(t) {
            var i = $(t.target);
            i.is("img") && i.attr({
                src: e,
                alt: "No photo",
                rel: "nofollow",
                "data-original": i.attr("src")
            }).addClass("broken").closest("figure").addClass("placeholder")
        }, !0), document.body.addEventListener("load", function(e) {
            var t = $(e.target);
            t.is("img") && t.closest("figure").addClass("loaded")
        }, !0);
        $("figure img").each(function() {
            var t = $(this).addClass("static");
            t.is(":hidden") || t.attr({
                "data-src-retina": t.attr("src"),
                "data-src": t.attr("src"),
                src: e
            }).unveil(200)
        });
        "_q" in window && ($.each(window._q, function(e, t) {
            $(t)
        }), delete _q)
    }(), $(function() {
        var e = $(window),
            t = $("body"),
            i = $("#top");
        $.ajaxSetup({
            statusCode: {
                401: function() {
                    WVN.alert(WVN.trans("msg_login_prompt"), function() {
                        window.location = WVN.url("ajax_redirect_to_login")
                    })
                }
            }
        }), WVN.cookies.get("timezone") || $.post(WVN.url("ajax_update_timezone"), {
            timezone: WVN.timezone
        }, "json");
        var n = $('<div class="overlay" style="display: none;"></div>').appendTo("body");
        Modernizr.touch && n.css("cursor", "pointer");
        $("ul.top-menu").find("li[data-overlay]");
        WVN.ratings(".ratings"), $("ul.checkbox-list input:disabled").closest("li").addClass("disabled"), $("ul.checkbox-list li.disabled input").prop("disabled", !0), WVN.select2("input[autocomplete], select[autocomplete]");
        $(".product-grid .item:visible").each(function() {
            var e = $(this);
            e.css("max-height", Math.ceil(e.height()) + "px")
        }), WVN.truncate(".item label, .equipment-item label", {
            after: ".badge"
        }), WVN.truncate("ul.checkbox-list > li > label", {
            after: ".count"
        });
        var c = $("input.toggle-checkbox");
        c.length && c.each(function() {
            var e = $(this),
                t = $(e.data("target"));
            if (t.length) {
                var i = t.find('input[type="checkbox"]');
                e.change(function() {
                    i.not(":disabled").prop("checked", e.prop("checked"))
                }), i.change(function() {
                    var t = !0;
                    i.not(":disabled").each(function() {
                        if (!$(this).prop("checked")) return t = !1, !1
                    }), e.prop("checked", t)
                })
            }
        });
        var d = $("table.fixed-headers");
        d.length && "floatThead" in d && (d.floatThead({
            useAbsolutePositioning: !1,
            zIndex: 4,
            scrollingTop: 55
        }), e.on("resize", function() {
            d.floatThead("reflow")
        }));
        var u = $(".maps");
        if (u.length) {
            var p = function() {
                u.each(function() {
                    var e = $(this),
                        t = e.data("name"),
                        i = e.data("address") || "",
                        n = e.data("latitude") || "",
                        a = e.data("longitude") || "",
                        o = e.data("width"),
                        r = e.data("height");
                    "" != n && "" != a && (i = [n, a]), WVN.createMap(e, i, t, o, r)
                })
            };
            e.one("mapsready", p), p()
        }
        var h = "input[required]:not(:radio,:file,:checkbox)";
        t.on("change input refresh", h, function() {
            var e = $(this),
                t = e.val(),
                i = e.siblings(".placeholder");
            "" == t ? i.show() : i.hide()
        }), t.on("init", h, function() {
            var e = $(this),
                t = e.attr("placeholder") || "",
                i = $('<span class="placeholder" />').text(t).hide();
            e.removeAttr("placeholder").after(i), e.trigger("refresh")
        }), $(h).trigger("init");
        var f = $("#gotop");
        if (f.length || i.length) {
            var g = !0;
            f.click(function() {
                var e = $(this).attr("href"),
                    t = "#" == e || "" == e ? "html" : e;
                return WVN.scrollTo(t), !1
            }), e.scroll(function() {
                var e = $(this).scrollTop();
                e > 20 && !g ? f.removeClass("hidden") : (g = !1, f.addClass("hidden"))
            }).scroll()
        }
        setTimeout(function() {
            e.trigger("scroll")
        }, 200), MODULES.initQuickContactModule(), $("html").addClass("pageloaded"), $("#top").click(function(e) {
            "container" != e.target.className && "top" != e.target.id && "img" != e.target.localName && "language_button" != e.target.id || n.trigger("click")
        })
    });