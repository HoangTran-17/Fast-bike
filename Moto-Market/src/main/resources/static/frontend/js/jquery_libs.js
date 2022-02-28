/*
 *  jQuery dotdotdot 1.7.4
 *
 *  Copyright (c) Fred Heusschen
 *  www.frebsite.nl
 *
 *  Plugin website:
 *  dotdotdot.frebsite.nl
 *
 *  Licensed under the MIT license.
 *  http://en.wikipedia.org/wiki/MIT_License
 */

(function($, undef) {
    if ($.fn.dotdotdot) {
        return;
    }

    $.fn.dotdotdot = function(o) {
        if (this.length == 0) {
            $.fn.dotdotdot.debug('No element found for "' + this.selector + '".');
            return this;
        }
        if (this.length > 1) {
            return this.each(
                function() {
                    $(this).dotdotdot(o);
                }
            );
        }

        var $dot = this;

        if ($dot.data('dotdotdot')) {
            $dot.trigger('destroy.dot');
        }

        $dot.data('dotdotdot-style', $dot.attr('style') || '');
        $dot.css('word-wrap', 'break-word');
        if ($dot.css('white-space') === 'nowrap') {
            $dot.css('white-space', 'normal');
        }

        $dot.bind_events = function() {
            $dot.bind(
                'update.dot',
                function(e, c) {
                    $dot.removeClass("is-truncated");
                    e.preventDefault();
                    e.stopPropagation();

                    switch (typeof opts.height) {
                        case 'number':
                            opts.maxHeight = opts.height;
                            break;

                        case 'function':
                            opts.maxHeight = opts.height.call($dot[0]);
                            break;

                        default:
                            opts.maxHeight = getTrueInnerHeight($dot);
                            break;
                    }

                    opts.maxHeight += opts.tolerance;

                    if (typeof c != 'undefined') {
                        if (typeof c == 'string' || ('nodeType' in c && c.nodeType === 1)) {
                            c = $('<div />').append(c).contents();
                        }
                        if (c instanceof $) {
                            orgContent = c;
                        }
                    }

                    $inr = $dot.wrapInner('<div class="dotdotdot" />').children();
                    $inr.contents()
                        .detach()
                        .end()
                        .append(orgContent.clone(true))
                        .find('br')
                        .replaceWith('  <br />  ')
                        .end()
                        .css({
                            'height': 'auto',
                            'width': 'auto',
                            'border': 'none',
                            'padding': 0,
                            'margin': 0
                        });

                    var after = false,
                        trunc = false;

                    if (conf.afterElement) {
                        after = conf.afterElement.clone(true);
                        after.show();
                        conf.afterElement.detach();
                    }

                    if (test($inr, opts)) {
                        if (opts.wrap == 'children') {
                            trunc = children($inr, opts, after);
                        } else {
                            trunc = ellipsis($inr, $dot, $inr, opts, after);
                        }
                    }
                    $inr.replaceWith($inr.contents());
                    $inr = null;

                    if ($.isFunction(opts.callback)) {
                        opts.callback.call($dot[0], trunc, orgContent);
                    }

                    conf.isTruncated = trunc;
                    return trunc;
                }

            ).bind(
                'isTruncated.dot',
                function(e, fn) {
                    e.preventDefault();
                    e.stopPropagation();

                    if (typeof fn == 'function') {
                        fn.call($dot[0], conf.isTruncated);
                    }
                    return conf.isTruncated;
                }

            ).bind(
                'originalContent.dot',
                function(e, fn) {
                    e.preventDefault();
                    e.stopPropagation();

                    if (typeof fn == 'function') {
                        fn.call($dot[0], orgContent);
                    }
                    return orgContent;
                }

            ).bind(
                'destroy.dot',
                function(e) {
                    e.preventDefault();
                    e.stopPropagation();

                    $dot.unwatch()
                        .unbind_events()
                        .contents()
                        .detach()
                        .end()
                        .append(orgContent)
                        .attr('style', $dot.data('dotdotdot-style') || '')
                        .data('dotdotdot', false);
                }
            );
            return $dot;
        }; //  /bind_events

        $dot.unbind_events = function() {
            $dot.unbind('.dot');
            return $dot;
        }; //  /unbind_events

        $dot.watch = function() {
            $dot.unwatch();
            if (opts.watch == 'window') {
                var $window = $(window),
                    _wWidth = $window.width(),
                    _wHeight = $window.height();

                $window.bind(
                    'resize.dot' + conf.dotId,
                    function() {
                        if (_wWidth != $window.width() || _wHeight != $window.height() || !opts.windowResizeFix) {
                            _wWidth = $window.width();
                            _wHeight = $window.height();

                            if (watchInt) {
                                clearInterval(watchInt);
                            }
                            watchInt = setTimeout(
                                function() {
                                    $dot.trigger('update.dot');
                                }, 100
                            );
                        }
                    }
                );
            } else {
                watchOrg = getSizes($dot);
                watchInt = setInterval(
                    function() {
                        if ($dot.is(':visible')) {
                            var watchNew = getSizes($dot);
                            if (watchOrg.width != watchNew.width ||
                                watchOrg.height != watchNew.height) {
                                $dot.trigger('update.dot');
                                watchOrg = watchNew;
                            }
                        }
                    }, 500
                );
            }
            return $dot;
        };
        $dot.unwatch = function() {
            $(window).unbind('resize.dot' + conf.dotId);
            if (watchInt) {
                clearInterval(watchInt);
            }
            return $dot;
        };

        var orgContent = $dot.contents(),
            opts = $.extend(true, {}, $.fn.dotdotdot.defaults, o),
            conf = {},
            watchOrg = {},
            watchInt = null,
            $inr = null;

        if (!(opts.lastCharacter.remove instanceof Array)) {
            opts.lastCharacter.remove = $.fn.dotdotdot.defaultArrays.lastCharacter.remove;
        }
        if (!(opts.lastCharacter.noEllipsis instanceof Array)) {
            opts.lastCharacter.noEllipsis = $.fn.dotdotdot.defaultArrays.lastCharacter.noEllipsis;
        }

        conf.afterElement = getElement(opts.after, $dot);
        conf.isTruncated = false;
        conf.dotId = dotId++;

        $dot.data('dotdotdot', true)
            .bind_events()
            .trigger('update.dot');

        if (opts.watch) {
            $dot.watch();
        }

        return $dot;
    };

    //  public
    $.fn.dotdotdot.defaults = {
        'ellipsis': '... ',
        'wrap': 'word',
        'fallbackToLetter': true,
        'lastCharacter': {},
        'tolerance': 0,
        'callback': null,
        'after': null,
        'height': null,
        'watch': false,
        'windowResizeFix': true
    };
    $.fn.dotdotdot.defaultArrays = {
        'lastCharacter': {
            'remove': [' ', '\u3000', ',', ';', '.', '!', '?'],
            'noEllipsis': []
        }
    };
    $.fn.dotdotdot.debug = function(msg) {};

    //  private
    var dotId = 1;

    function children($elem, o, after) {
        var $elements = $elem.children(),
            isTruncated = false;

        $elem.empty();

        for (var a = 0, l = $elements.length; a < l; a++) {
            var $e = $elements.eq(a);
            $elem.append($e);
            if (after) {
                $elem.append(after);
            }
            if (test($elem, o)) {
                $e.remove();
                isTruncated = true;
                break;
            } else {
                if (after) {
                    after.detach();
                }
            }
        }
        return isTruncated;
    }

    function ellipsis($elem, $d, $i, o, after) {
        var isTruncated = false;

        //  Don't put the ellipsis directly inside these elements
        var notx = 'a, table, thead, tbody, tfoot, tr, col, colgroup, object, embed, param, ol, ul, dl, blockquote, select, optgroup, option, textarea, script, style';

        //  Don't remove these elements even if they are after the ellipsis
        var noty = 'script, .dotdotdot-keep';

        $elem
            .contents()
            .detach()
            .each(
                function() {

                    var e = this,
                        $e = $(e);

                    if (typeof e == 'undefined') {
                        return true;
                    } else if ($e.is(noty)) {
                        $elem.append($e);
                    } else if (isTruncated) {
                        return true;
                    } else {
                        $elem.append($e);
                        if (after && !$e.is(o.after) && !$e.find(o.after).length) {
                            $elem[$elem.is(notx) ? 'after' : 'append'](after);
                        }
                        if (test($i, o)) {
                            if (e.nodeType == 3) // node is TEXT
                            {
                                isTruncated = ellipsisElement($e, $d, $i, o, after);
                            } else {
                                isTruncated = ellipsis($e, $d, $i, o, after);
                            }

                            if (!isTruncated) {
                                $e.detach();
                                isTruncated = true;
                            }
                        }

                        if (!isTruncated) {
                            if (after) {
                                after.detach();
                            }
                        }
                    }
                }
            );
        $d.addClass("is-truncated");
        return isTruncated;
    }

    function ellipsisElement($e, $d, $i, o, after) {
        var e = $e[0];

        if (!e) {
            return false;
        }

        var txt = getTextContent(e),
            space = (txt.indexOf(' ') !== -1) ? ' ' : '\u3000',
            separator = (o.wrap == 'letter') ? '' : space,
            textArr = txt.split(separator),
            position = -1,
            midPos = -1,
            startPos = 0,
            endPos = textArr.length - 1;

        //  Only one word
        if (o.fallbackToLetter && startPos == 0 && endPos == 0) {
            separator = '';
            textArr = txt.split(separator);
            endPos = textArr.length - 1;
        }

        while (startPos <= endPos && !(startPos == 0 && endPos == 0)) {
            var m = Math.floor((startPos + endPos) / 2);
            if (m == midPos) {
                break;
            }
            midPos = m;

            setTextContent(e, textArr.slice(0, midPos + 1).join(separator) + o.ellipsis);
            $i.children()
                .each(
                    function() {
                        $(this).toggle().toggle();
                    }
                );

            if (!test($i, o)) {
                position = midPos;
                startPos = midPos;
            } else {
                endPos = midPos;

                //  Fallback to letter
                if (o.fallbackToLetter && startPos == 0 && endPos == 0) {
                    separator = '';
                    textArr = textArr[0].split(separator);
                    position = -1;
                    midPos = -1;
                    startPos = 0;
                    endPos = textArr.length - 1;
                }
            }
        }

        if (position != -1 && !(textArr.length == 1 && textArr[0].length == 0)) {
            txt = addEllipsis(textArr.slice(0, position + 1).join(separator), o);
            setTextContent(e, txt);
        } else {
            var $w = $e.parent();
            $e.detach();

            var afterLength = (after && after.closest($w).length) ? after.length : 0;

            if ($w.contents().length > afterLength) {
                e = findLastTextNode($w.contents().eq(-1 - afterLength), $d);
            } else {
                e = findLastTextNode($w, $d, true);
                if (!afterLength) {
                    $w.detach();
                }
            }
            if (e) {
                txt = addEllipsis(getTextContent(e), o);
                setTextContent(e, txt);
                if (afterLength && after) {
                    $(e).parent().append(after);
                }
            }
        }

        return true;
    }

    function test($i, o) {
        return $i.innerHeight() > o.maxHeight;
    }

    function addEllipsis(txt, o) {
        while ($.inArray(txt.slice(-1), o.lastCharacter.remove) > -1) {
            txt = txt.slice(0, -1);
        }
        if ($.inArray(txt.slice(-1), o.lastCharacter.noEllipsis) < 0) {
            txt += o.ellipsis;
        }
        return txt;
    }

    function getSizes($d) {
        return {
            'width': $d.innerWidth(),
            'height': $d.innerHeight()
        };
    }

    function setTextContent(e, content) {
        if (e.innerText) {
            e.innerText = content;
        } else if (e.nodeValue) {
            e.nodeValue = content;
        } else if (e.textContent) {
            e.textContent = content;
        }

    }

    function getTextContent(e) {
        if (e.innerText) {
            return e.innerText;
        } else if (e.nodeValue) {
            return e.nodeValue;
        } else if (e.textContent) {
            return e.textContent;
        } else {
            return "";
        }
    }

    function getPrevNode(n) {
        do {
            n = n.previousSibling;
        }
        while (n && n.nodeType !== 1 && n.nodeType !== 3);

        return n;
    }

    function findLastTextNode($el, $top, excludeCurrent) {
        var e = $el && $el[0],
            p;
        if (e) {
            if (!excludeCurrent) {
                if (e.nodeType === 3) {
                    return e;
                }
                if ($.trim($el.text())) {
                    return findLastTextNode($el.contents().last(), $top);
                }
            }
            p = getPrevNode(e);
            while (!p) {
                $el = $el.parent();
                if ($el.is($top) || !$el.length) {
                    return false;
                }
                p = getPrevNode($el[0]);
            }
            if (p) {
                return findLastTextNode($(p), $top);
            }
        }
        return false;
    }

    function getElement(e, $i) {
        if (!e) {
            return false;
        }
        if (typeof e === 'string') {
            e = $(e, $i);
            return (e.length) ? e : false;
        }
        return !e.jquery ? false : e;
    }

    function getTrueInnerHeight($el) {
        var h = $el.innerHeight(),
            a = ['paddingTop', 'paddingBottom'];

        for (var z = 0, l = a.length; z < l; z++) {
            var m = parseInt($el.css(a[z]), 10);
            if (isNaN(m)) {
                m = 0;
            }
            h -= m;
        }
        return h;
    }

    //  override jQuery.html
    var _orgHtml = $.fn.html;
    $.fn.html = function(str) {
        if (str != undef && !$.isFunction(str) && this.data('dotdotdot')) {
            return this.trigger('update', [str]);
        }
        return _orgHtml.apply(this, arguments);
    };

    //  override jQuery.text
    var _orgText = $.fn.text;
    $.fn.text = function(str) {
        if (str != undef && !$.isFunction(str) && this.data('dotdotdot')) {
            str = $('<div />').text(str).html();
            return this.trigger('update', [str]);
        }
        return _orgText.apply(this, arguments);
    };

})(jQuery);

/*
 * jQuery EasyTabs plugin 3.2.0
 *
 * Copyright (c) 2010-2011 Steve Schwartz (JangoSteve)
 *
 * Dual licensed under the MIT and GPL licenses:
 *   http://www.opensource.org/licenses/mit-license.php
 *   http://www.gnu.org/licenses/gpl.html
 *
 * Date: Thu May 09 17:30:00 2013 -0500
 */
(function($) {

    $.easytabs = function(container, options) {

        // Attach to plugin anything that should be available via
        // the $container.data('easytabs') object
        var plugin = this,
            $container = $(container),

            defaults = {
                animate: true,
                panelActiveClass: "active",
                tabActiveClass: "active",
                defaultTab: "li:first-child",
                animationSpeed: "normal",
                tabs: "> ul > li",
                updateHash: true,
                cycle: false,
                collapsible: false,
                collapsedClass: "collapsed",
                collapsedByDefault: true,
                uiTabs: false,
                transitionIn: 'fadeIn',
                transitionOut: 'fadeOut',
                transitionInEasing: 'swing',
                transitionOutEasing: 'swing',
                transitionCollapse: 'slideUp',
                transitionUncollapse: 'slideDown',
                transitionCollapseEasing: 'swing',
                transitionUncollapseEasing: 'swing',
                containerClass: "",
                tabsClass: "",
                tabClass: "",
                panelClass: "",
                cache: true,
                event: 'click',
                panelContext: $container
            },

            // Internal instance variables
            // (not available via easytabs object)
            $defaultTab,
            $defaultTabLink,
            transitions,
            lastHash,
            skipUpdateToHash,
            animationSpeeds = {
                fast: 200,
                normal: 400,
                slow: 600
            },

            // Shorthand variable so that we don't need to call
            // plugin.settings throughout the plugin code
            settings;

        // =============================================================
        // Functions available via easytabs object
        // =============================================================

        plugin.init = function() {

            plugin.settings = settings = $.extend({}, defaults, options);
            settings.bind_str = settings.event + ".easytabs";

            // Add jQuery UI's crazy class names to markup,
            // so that markup will match theme CSS
            if (settings.uiTabs) {
                settings.tabActiveClass = 'ui-tabs-selected';
                settings.containerClass = 'ui-tabs ui-widget ui-widget-content ui-corner-all';
                settings.tabsClass = 'ui-tabs-nav ui-helper-reset ui-helper-clearfix ui-widget-header ui-corner-all';
                settings.tabClass = 'ui-state-default ui-corner-top';
                settings.panelClass = 'ui-tabs-panel ui-widget-content ui-corner-bottom';
            }

            // If collapsible is true and defaultTab specified, assume user wants defaultTab showing (not collapsed)
            if (settings.collapsible && options.defaultTab !== undefined && options.collpasedByDefault === undefined) {
                settings.collapsedByDefault = false;
            }

            // Convert 'normal', 'fast', and 'slow' animation speed settings to their respective speed in milliseconds
            if (typeof(settings.animationSpeed) === 'string') {
                settings.animationSpeed = animationSpeeds[settings.animationSpeed];
            }

            $('a.anchor').remove().prependTo('body');

            // Store easytabs object on container so we can easily set
            // properties throughout
            $container.data('easytabs', {});

            plugin.setTransitions();

            plugin.getTabs();

            addClasses();

            setDefaultTab();

            bindToTabClicks();

            initHashChange();

            initCycle();

            // Append data-easytabs HTML attribute to make easy to query for
            // easytabs instances via CSS pseudo-selector
            $container.attr('data-easytabs', true);
        };

        // Set transitions for switching between tabs based on options.
        // Could be used to update transitions if settings are changes.
        plugin.setTransitions = function() {
            transitions = (settings.animate) ? {
                show: settings.transitionIn,
                hide: settings.transitionOut,
                speed: settings.animationSpeed,
                collapse: settings.transitionCollapse,
                uncollapse: settings.transitionUncollapse,
                halfSpeed: settings.animationSpeed / 2
            } : {
                show: "show",
                hide: "hide",
                speed: 0,
                collapse: "hide",
                uncollapse: "show",
                halfSpeed: 0
            };
        };

        // Find and instantiate tabs and panels.
        // Could be used to reset tab and panel collection if markup is
        // modified.
        plugin.getTabs = function() {
            var $matchingPanel;

            // Find the initial set of elements matching the setting.tabs
            // CSS selector within the container
            plugin.tabs = $container.find(settings.tabs),

                // Instantiate panels as empty jquery object
                plugin.panels = $(),

                plugin.tabs.each(function() {
                    var $tab = $(this),
                        $a = $tab.children('a'),

                        // targetId is the ID of the panel, which is either the
                        // `href` attribute for non-ajax tabs, or in the
                        // `data-target` attribute for ajax tabs since the `href` is
                        // the ajax URL
                        targetId = $tab.children('a').data('target');

                    $tab.data('easytabs', {});

                    // If the tab has a `data-target` attribute, and is thus an ajax tab
                    if (targetId !== undefined && targetId !== null) {
                        $tab.data('easytabs').ajax = $a.attr('href');
                    } else {
                        targetId = $a.attr('href');
                    }
                    targetId = targetId.match(/#([^\?]+)/)[1];

                    $matchingPanel = settings.panelContext.find("#" + targetId);

                    // If tab has a matching panel, add it to panels
                    if ($matchingPanel.length) {

                        // Store panel height before hiding
                        $matchingPanel.data('easytabs', {
                            position: $matchingPanel.css('position'),
                            visibility: $matchingPanel.css('visibility')
                        });

                        // Don't hide panel if it's active (allows `getTabs` to be called manually to re-instantiate tab collection)
                        $matchingPanel.not(settings.panelActiveClass).hide();

                        plugin.panels = plugin.panels.add($matchingPanel);

                        $tab.data('easytabs').panel = $matchingPanel;

                        // Otherwise, remove tab from tabs collection
                    } else {
                        plugin.tabs = plugin.tabs.not($tab);
                        if ('console' in window) {
                            console.warn('Warning: tab without matching panel for selector \'#' + targetId + '\' removed from set');
                        }
                    }
                });
        };

        // Select tab and fire callback
        plugin.selectTab = function($clicked, callback) {
            var url = window.location,
                hash = url.hash.match(/^[^\?]*/)[0],
                $targetPanel = $clicked.parent().data('easytabs').panel,
                ajaxUrl = $clicked.parent().data('easytabs').ajax;

            // Tab is collapsible and active => toggle collapsed state
            if (settings.collapsible && !skipUpdateToHash && ($clicked.hasClass(settings.tabActiveClass) || $clicked.hasClass(settings.collapsedClass))) {
                plugin.toggleTabCollapse($clicked, $targetPanel, ajaxUrl, callback);

                // Tab is not active and panel is not active => select tab
            } else if (!$clicked.hasClass(settings.tabActiveClass) || !$targetPanel.hasClass(settings.panelActiveClass)) {
                activateTab($clicked, $targetPanel, ajaxUrl, callback);

                // Cache is disabled => reload (e.g reload an ajax tab).
            } else if (!settings.cache) {
                activateTab($clicked, $targetPanel, ajaxUrl, callback);
            }

        };

        // Toggle tab collapsed state and fire callback
        plugin.toggleTabCollapse = function($clicked, $targetPanel, ajaxUrl, callback) {
            plugin.panels.stop(true, true);

            if (fire($container, "easytabs:before", [$clicked, $targetPanel, settings])) {
                plugin.tabs.filter("." + settings.tabActiveClass).removeClass(settings.tabActiveClass).children().removeClass(settings.tabActiveClass);

                // If panel is collapsed, uncollapse it
                if ($clicked.hasClass(settings.collapsedClass)) {

                    // If ajax panel and not already cached
                    if (ajaxUrl && (!settings.cache || !$clicked.parent().data('easytabs').cached)) {
                        $container.trigger('easytabs:ajax:beforeSend', [$clicked, $targetPanel]);

                        $targetPanel.load(ajaxUrl, function(response, status, xhr) {
                            $clicked.parent().data('easytabs').cached = true;
                            $container.trigger('easytabs:ajax:complete', [$clicked, $targetPanel, response, status, xhr]);
                        });
                    }

                    // Update CSS classes of tab and panel
                    $clicked.parent()
                        .removeClass(settings.collapsedClass)
                        .addClass(settings.tabActiveClass)
                        .children()
                        .removeClass(settings.collapsedClass)
                        .addClass(settings.tabActiveClass);

                    $targetPanel
                        .addClass(settings.panelActiveClass)[transitions.uncollapse](transitions.speed, settings.transitionUncollapseEasing, function() {
                            $container.trigger('easytabs:midTransition', [$clicked, $targetPanel, settings]);
                            if (typeof callback == 'function') callback();
                        });

                    // Otherwise, collapse it
                } else {

                    // Update CSS classes of tab and panel
                    $clicked.addClass(settings.collapsedClass)
                        .parent()
                        .addClass(settings.collapsedClass);

                    $targetPanel
                        .removeClass(settings.panelActiveClass)[transitions.collapse](transitions.speed, settings.transitionCollapseEasing, function() {
                            $container.trigger("easytabs:midTransition", [$clicked, $targetPanel, settings]);
                            if (typeof callback == 'function') callback();
                        });
                }
            }
        };

        // Find tab with target panel matching value
        plugin.matchTab = function(hash) {
            return plugin.tabs.find("[href='" + hash + "'],[data-target='" + hash + "']").first();
        };

        // Find panel with `id` matching value
        plugin.matchInPanel = function(hash) {
            return (hash && plugin.validId(hash) ? plugin.panels.filter(':has(' + hash + ')').first() : []);
        };

        // Make sure hash is a valid id value (admittedly strict in that HTML5 allows almost anything without a space)
        // but jQuery has issues with such id values anyway, so we can afford to be strict here.
        plugin.validId = function(id) {
            return id.substr(1).match(/^[A-Za-z][A-Za-z0-9\-_:\.]*$/);
        };

        // Select matching tab when URL hash changes
        plugin.selectTabFromHashChange = function() {
            var hash = window.location.hash.match(/^[^\?]*/)[0],
                $tab = plugin.matchTab(hash),
                $panel;

            if (settings.updateHash) {

                // If hash directly matches tab
                if ($tab.length) {
                    skipUpdateToHash = true;
                    plugin.selectTab($tab);

                } else {
                    $panel = plugin.matchInPanel(hash);

                    // If panel contains element matching hash
                    if ($panel.length) {
                        hash = '#' + $panel.attr('id');
                        $tab = plugin.matchTab(hash);
                        skipUpdateToHash = true;
                        plugin.selectTab($tab);

                        // If default tab is not active...
                    } else if (!$defaultTab.hasClass(settings.tabActiveClass) && !settings.cycle) {

                        // ...and hash is blank or matches a parent of the tab container or
                        // if the last tab (before the hash updated) was one of the other tabs in this container.
                        if (hash === '' || plugin.matchTab(lastHash).length || $container.closest(hash).length) {
                            skipUpdateToHash = true;
                            plugin.selectTab($defaultTabLink);
                        }
                    }
                }
            }
        };

        // Cycle through tabs
        plugin.cycleTabs = function(tabNumber) {
            if (settings.cycle) {
                tabNumber = tabNumber % plugin.tabs.length;
                $tab = $(plugin.tabs[tabNumber]).children("a").first();
                skipUpdateToHash = true;
                plugin.selectTab($tab, function() {
                    setTimeout(function() {
                        plugin.cycleTabs(tabNumber + 1);
                    }, settings.cycle);
                });
            }
        };

        // Convenient public methods
        plugin.publicMethods = {
            select: function(tabSelector) {
                var $tab;

                // Find tab container that matches selector (like 'li#tab-one' which contains tab link)
                if (($tab = plugin.tabs.filter(tabSelector)).length === 0) {

                    // Find direct tab link that matches href (like 'a[href="#panel-1"]')
                    if (($tab = plugin.tabs.find("a[href='" + tabSelector + "']")).length === 0) {

                        // Find direct tab link that matches selector (like 'a#tab-1')
                        if (($tab = plugin.tabs.find("a" + tabSelector)).length === 0) {

                            // Find direct tab link that matches data-target (lik 'a[data-target="#panel-1"]')
                            if (($tab = plugin.tabs.find("[data-target='" + tabSelector + "']")).length === 0) {

                                // Find direct tab link that ends in the matching href (like 'a[href$="#panel-1"]', which would also match http://example.com/currentpage/#panel-1)
                                if (($tab = plugin.tabs.find("a[href$='" + tabSelector + "']")).length === 0) {

                                    $.error('Tab \'' + tabSelector + '\' does not exist in tab set');
                                }
                            }
                        }
                    }
                } else {
                    // Select the child tab link, since the first option finds the tab container (like <li>)
                    $tab = $tab.children("a").first();
                }
                plugin.selectTab($tab);
            }
        };

        // =============================================================
        // Private functions
        // =============================================================

        // Triggers an event on an element and returns the event result
        var fire = function(obj, name, data) {
            var event = $.Event(name);
            obj.trigger(event, data);
            return event.result !== false;
        }

        // Add CSS classes to markup (if specified), called by init
        var addClasses = function() {
            $container.addClass(settings.containerClass);
            plugin.tabs.parent().addClass(settings.tabsClass);
            plugin.tabs.addClass(settings.tabClass);
            plugin.panels.addClass(settings.panelClass);
        };

        // Set the default tab, whether from hash (bookmarked) or option,
        // called by init
        var setDefaultTab = function() {
            var hash = window.location.hash.match(/^[^\?]*/)[0],
                $selectedTab = plugin.matchTab(hash).parent(),
                $panel;

            // If hash directly matches one of the tabs, active on page-load
            if ($selectedTab.length === 1) {
                $defaultTab = $selectedTab;
                settings.cycle = false;

            } else {
                $panel = plugin.matchInPanel(hash);

                // If one of the panels contains the element matching the hash,
                // make it active on page-load
                if ($panel.length) {
                    hash = '#' + $panel.attr('id');
                    $defaultTab = plugin.matchTab(hash).parent();

                    // Otherwise, make the default tab the one that's active on page-load
                } else {
                    $defaultTab = plugin.tabs.parent().find(settings.defaultTab);
                    if ($defaultTab.length === 0) {
                        $.error("The specified default tab ('" + settings.defaultTab + "') could not be found in the tab set ('" + settings.tabs + "') out of " + plugin.tabs.length + " tabs.");
                    }
                }
            }

            $defaultTabLink = $defaultTab.children("a").first();

            activateDefaultTab($selectedTab);
        };

        // Activate defaultTab (or collapse by default), called by setDefaultTab
        var activateDefaultTab = function($selectedTab) {
            var defaultPanel,
                defaultAjaxUrl;

            if (settings.collapsible && $selectedTab.length === 0 && settings.collapsedByDefault) {
                $defaultTab
                    .addClass(settings.collapsedClass)
                    .children()
                    .addClass(settings.collapsedClass);

            } else {

                defaultPanel = $($defaultTab.data('easytabs').panel);
                defaultAjaxUrl = $defaultTab.data('easytabs').ajax;

                if (defaultAjaxUrl && (!settings.cache || !$defaultTab.data('easytabs').cached)) {
                    $container.trigger('easytabs:ajax:beforeSend', [$defaultTabLink, defaultPanel]);
                    defaultPanel.load(defaultAjaxUrl, function(response, status, xhr) {
                        $defaultTab.data('easytabs').cached = true;
                        $container.trigger('easytabs:ajax:complete', [$defaultTabLink, defaultPanel, response, status, xhr]);
                    });
                }

                $defaultTab.data('easytabs').panel
                    .show()
                    .addClass(settings.panelActiveClass);

                $defaultTab
                    .addClass(settings.tabActiveClass)
                    .children()
                    .addClass(settings.tabActiveClass);
            }

            // Fire event when the plugin is initialised
            $container.trigger("easytabs:initialised", [$defaultTabLink, defaultPanel]);
        };

        // Bind tab-select funtionality to namespaced click event, called by
        // init
        var bindToTabClicks = function() {
            plugin.tabs.children("a").bind(settings.bind_str, function(e) {

                // Stop cycling when a tab is clicked
                settings.cycle = false;

                // Hash will be updated when tab is clicked,
                // don't cause tab to re-select when hash-change event is fired
                skipUpdateToHash = false;

                // Select the panel for the clicked tab
                plugin.selectTab($(this));

                // Don't follow the link to the anchor
                e.preventDefault ? e.preventDefault() : e.returnValue = false;
            });
        };

        // Activate a given tab/panel, called from plugin.selectTab:
        //
        //   * fire `easytabs:before` hook
        //   * get ajax if new tab is an uncached ajax tab
        //   * animate out previously-active panel
        //   * fire `easytabs:midTransition` hook
        //   * update URL hash
        //   * animate in newly-active panel
        //   * update CSS classes for inactive and active tabs/panels
        //
        // TODO: This could probably be broken out into many more modular
        // functions
        var activateTab = function($clicked, $targetPanel, ajaxUrl, callback) {
            plugin.panels.stop(true, true);

            if (fire($container, "easytabs:before", [$clicked, $targetPanel, settings])) {
                var $visiblePanel = plugin.panels.filter(":visible"),
                    $panelContainer = $targetPanel.parent(),
                    targetHeight,
                    visibleHeight,
                    heightDifference,
                    showPanel,
                    hash = window.location.hash.match(/^[^\?]*/)[0];

                if (settings.animate) {
                    targetHeight = getHeightForHidden($targetPanel);
                    visibleHeight = $visiblePanel.length ? setAndReturnHeight($visiblePanel) : 0;
                    heightDifference = targetHeight - visibleHeight;
                }

                // Set lastHash to help indicate if defaultTab should be
                // activated across multiple tab instances.
                lastHash = hash;

                // TODO: Move this function elsewhere
                showPanel = function() {
                    // At this point, the previous panel is hidden, and the new one will be selected
                    $container.trigger("easytabs:midTransition", [$clicked, $targetPanel, settings]);

                    // Gracefully animate between panels of differing heights, start height change animation *after* panel change if panel needs to contract,
                    // so that there is no chance of making the visible panel overflowing the height of the target panel
                    if (settings.animate && settings.transitionIn == 'fadeIn') {
                        if (heightDifference < 0)
                            $panelContainer.animate({
                                height: $panelContainer.height() + heightDifference
                            }, transitions.halfSpeed).css({
                                'min-height': ''
                            });
                    }

                    if (settings.updateHash && !skipUpdateToHash) {
                        //window.location = url.toString().replace((url.pathname + hash), (url.pathname + $clicked.attr("href")));
                        // Not sure why this behaves so differently, but it's more straight forward and seems to have less side-effects
                        if (window.history.pushState) {
                            window.history.pushState(null, null, '#' + $targetPanel.attr('id'));
                        } else {
                            window.location.hash = '#' + $targetPanel.attr('id');
                        }
                    } else {
                        skipUpdateToHash = false;
                    }

                    $targetPanel
                        [transitions.show](transitions.speed, settings.transitionInEasing, function() {
                            $panelContainer.css({
                                height: '',
                                'min-height': ''
                            }); // After the transition, unset the height
                            $container.trigger("easytabs:after", [$clicked, $targetPanel, settings]);
                            // callback only gets called if selectTab actually does something, since it's inside the if block
                            if (typeof callback == 'function') {
                                callback();
                            }
                        });
                };

                if (ajaxUrl && (!settings.cache || !$clicked.parent().data('easytabs').cached)) {
                    $container.trigger('easytabs:ajax:beforeSend', [$clicked, $targetPanel]);
                    $targetPanel.load(ajaxUrl, function(response, status, xhr) {
                        $clicked.parent().data('easytabs').cached = true;
                        $container.trigger('easytabs:ajax:complete', [$clicked, $targetPanel, response, status, xhr]);
                    });
                }

                // Gracefully animate between panels of differing heights, start height change animation *before* panel change if panel needs to expand,
                // so that there is no chance of making the target panel overflowing the height of the visible panel
                if (settings.animate && settings.transitionOut == 'fadeOut') {
                    if (heightDifference > 0) {
                        $panelContainer.animate({
                            height: ($panelContainer.height() + heightDifference)
                        }, transitions.halfSpeed);
                    } else {
                        // Prevent height jumping before height transition is triggered at midTransition
                        $panelContainer.css({
                            'min-height': $panelContainer.height()
                        });
                    }
                }

                // Change the active tab *first* to provide immediate feedback when the user clicks
                plugin.tabs.filter("." + settings.tabActiveClass).removeClass(settings.tabActiveClass).children().removeClass(settings.tabActiveClass);
                plugin.tabs.filter("." + settings.collapsedClass).removeClass(settings.collapsedClass).children().removeClass(settings.collapsedClass);
                $clicked.parent().addClass(settings.tabActiveClass).children().addClass(settings.tabActiveClass);

                plugin.panels.filter("." + settings.panelActiveClass).removeClass(settings.panelActiveClass);
                $targetPanel.addClass(settings.panelActiveClass);

                if ($visiblePanel.length) {
                    $visiblePanel
                        [transitions.hide](transitions.speed, settings.transitionOutEasing, showPanel);
                } else {
                    $targetPanel
                        [transitions.uncollapse](transitions.speed, settings.transitionUncollapseEasing, showPanel);
                }
            }
        };

        // Get heights of panels to enable animation between panels of
        // differing heights, called by activateTab
        var getHeightForHidden = function($targetPanel) {

            if ($targetPanel.data('easytabs') && $targetPanel.data('easytabs').lastHeight) {
                return $targetPanel.data('easytabs').lastHeight;
            }

            // this is the only property easytabs changes, so we need to grab its value on each tab change
            var display = $targetPanel.css('display'),
                outerCloak,
                height;

            // Workaround with wrapping height, because firefox returns wrong
            // height if element itself has absolute positioning.
            // but try/catch block needed for IE7 and IE8 because they throw
            // an "Unspecified error" when trying to create an element
            // with the css position set.
            try {
                outerCloak = $('<div></div>', {
                    'position': 'absolute',
                    'visibility': 'hidden',
                    'overflow': 'hidden'
                });
            } catch (e) {
                outerCloak = $('<div></div>', {
                    'visibility': 'hidden',
                    'overflow': 'hidden'
                });
            }
            height = $targetPanel
                .wrap(outerCloak)
                .css({
                    'position': 'relative',
                    'visibility': 'hidden',
                    'display': 'block'
                })
                .outerHeight();

            $targetPanel.unwrap();

            // Return element to previous state
            $targetPanel.css({
                position: $targetPanel.data('easytabs').position,
                visibility: $targetPanel.data('easytabs').visibility,
                display: display
            });

            // Cache height
            $targetPanel.data('easytabs').lastHeight = height;

            return height;
        };

        // Since the height of the visible panel may have been manipulated due to interaction,
        // we want to re-cache the visible height on each tab change, called
        // by activateTab
        var setAndReturnHeight = function($visiblePanel) {
            var height = $visiblePanel.outerHeight();

            if ($visiblePanel.data('easytabs')) {
                $visiblePanel.data('easytabs').lastHeight = height;
            } else {
                $visiblePanel.data('easytabs', {
                    lastHeight: height
                });
            }
            return height;
        };

        // Setup hash-change callback for forward- and back-button
        // functionality, called by init
        var initHashChange = function() {

            // enabling back-button with jquery.hashchange plugin
            // http://benalman.com/projects/jquery-hashchange-plugin/
            if (typeof $(window).hashchange === 'function') {
                $(window).hashchange(function() {
                    plugin.selectTabFromHashChange();
                });
            } else if ($.address && typeof $.address.change === 'function') { // back-button with jquery.address plugin http://www.asual.com/jquery/address/docs/
                $.address.change(function() {
                    plugin.selectTabFromHashChange();
                });
            }
        };

        // Begin cycling if set in options, called by init
        var initCycle = function() {
            var tabNumber;
            if (settings.cycle) {
                tabNumber = plugin.tabs.index($defaultTab);
                setTimeout(function() {
                    plugin.cycleTabs(tabNumber + 1);
                }, settings.cycle);
            }
        };

        plugin.init();

    };

    $.fn.easytabs = function(options) {
        var args = arguments;

        return this.each(function() {
            var $this = $(this),
                plugin = $this.data('easytabs');

            // Initialization was called with $(el).easytabs( { options } );
            if (undefined === plugin) {
                plugin = new $.easytabs(this, options);
                $this.data('easytabs', plugin);
            }

            // User called public method
            if (plugin.publicMethods[options]) {
                return plugin.publicMethods[options](Array.prototype.slice.call(args, 1));
            }
        });
    };

})(jQuery);

/*! fixto - v0.4.0 - 2015-06-08
 * http://github.com/bbarakaci/fixto/*/

var fixto = (function($, window, document) {

    // Start Computed Style. Please do not modify this module here. Modify it from its own repo. See address below.

    /*! Computed Style - v0.1.0 - 2012-07-19
     * https://github.com/bbarakaci/computed-style
     * Copyright (c) 2012 Burak Barakaci; Licensed MIT */
    var computedStyle = (function() {
        var computedStyle = {
            getAll: function(element) {
                return document.defaultView.getComputedStyle(element);
            },
            get: function(element, name) {
                return this.getAll(element)[name];
            },
            toFloat: function(value) {
                return parseFloat(value, 10) || 0;
            },
            getFloat: function(element, name) {
                return this.toFloat(this.get(element, name));
            },
            _getAllCurrentStyle: function(element) {
                return element.currentStyle;
            }
        };

        if (document.documentElement.currentStyle) {
            computedStyle.getAll = computedStyle._getAllCurrentStyle;
        }

        return computedStyle;

    }());

    // End Computed Style. Modify whatever you want to.

    var mimicNode = (function() {
        /*
        Class Mimic Node
        Dependency : Computed Style
        Tries to mimick a dom node taking his styles, dimensions. May go to his repo if gets mature.
        */

        function MimicNode(element) {
            this.element = element;
            this.replacer = document.createElement('div');
            this.replacer.style.visibility = 'hidden';
            this.hide();
            element.parentNode.insertBefore(this.replacer, element);
        }

        MimicNode.prototype = {
            replace: function() {
                var rst = this.replacer.style;
                var styles = computedStyle.getAll(this.element);

                // rst.width = computedStyle.width(this.element) + 'px';
                // rst.height = this.element.offsetHeight + 'px';

                // Setting offsetWidth
                rst.width = this._width();
                rst.height = this._height();

                // Adopt margins
                rst.marginTop = styles.marginTop;
                rst.marginBottom = styles.marginBottom;
                rst.marginLeft = styles.marginLeft;
                rst.marginRight = styles.marginRight;

                // Adopt positioning
                rst.cssFloat = styles.cssFloat;
                rst.styleFloat = styles.styleFloat; //ie8;
                rst.position = styles.position;
                rst.top = styles.top;
                rst.right = styles.right;
                rst.bottom = styles.bottom;
                rst.left = styles.left;
                // rst.borderStyle = styles.borderStyle;

                rst.display = styles.display;

            },

            hide: function() {
                this.replacer.style.display = 'none';
            },

            _width: function() {
                return this.element.getBoundingClientRect().width + 'px';
            },

            _widthOffset: function() {
                return this.element.offsetWidth + 'px';
            },

            _height: function() {
                return this.element.getBoundingClientRect().height + 'px';
            },

            _heightOffset: function() {
                return this.element.offsetHeight + 'px';
            },

            destroy: function() {
                $(this.replacer).remove();

                // set properties to null to break references
                for (var prop in this) {
                    if (this.hasOwnProperty(prop)) {
                        this[prop] = null;
                    }
                }
            }
        };

        var bcr = document.documentElement.getBoundingClientRect();
        if (!bcr.width) {
            MimicNode.prototype._width = MimicNode.prototype._widthOffset;
            MimicNode.prototype._height = MimicNode.prototype._heightOffset;
        }

        return {
            MimicNode: MimicNode,
            computedStyle: computedStyle
        };
    }());

    // Class handles vendor prefixes
    function Prefix() {
        // Cached vendor will be stored when it is detected
        this._vendor = null;

        //this._dummy = document.createElement('div');
    }

    Prefix.prototype = {

        _vendors: {
            webkit: {
                cssPrefix: '-webkit-',
                jsPrefix: 'Webkit'
            },
            moz: {
                cssPrefix: '-moz-',
                jsPrefix: 'Moz'
            },
            ms: {
                cssPrefix: '-ms-',
                jsPrefix: 'ms'
            },
            opera: {
                cssPrefix: '-o-',
                jsPrefix: 'O'
            }
        },

        _prefixJsProperty: function(vendor, prop) {
            return vendor.jsPrefix + prop[0].toUpperCase() + prop.substr(1);
        },

        _prefixValue: function(vendor, value) {
            return vendor.cssPrefix + value;
        },

        _valueSupported: function(prop, value, dummy) {
            // IE8 will throw Illegal Argument when you attempt to set a not supported value.
            try {
                dummy.style[prop] = value;
                return dummy.style[prop] === value;
            } catch (er) {
                return false;
            }
        },

        /**
         * Returns true if the property is supported
         * @param {string} prop Property name
         * @returns {boolean}
         */
        propertySupported: function(prop) {
            // Supported property will return either inine style value or an empty string.
            // Undefined means property is not supported.
            return document.documentElement.style[prop] !== undefined;
        },

        /**
         * Returns prefixed property name for js usage
         * @param {string} prop Property name
         * @returns {string|null}
         */
        getJsProperty: function(prop) {
            // Try native property name first.
            if (this.propertySupported(prop)) {
                return prop;
            }

            // Prefix it if we know the vendor already
            if (this._vendor) {
                return this._prefixJsProperty(this._vendor, prop);
            }

            // We don't know the vendor, try all the possibilities
            var prefixed;
            for (var vendor in this._vendors) {
                prefixed = this._prefixJsProperty(this._vendors[vendor], prop);
                if (this.propertySupported(prefixed)) {
                    // Vendor detected. Cache it.
                    this._vendor = this._vendors[vendor];
                    return prefixed;
                }
            }

            // Nothing worked
            return null;
        },

        /**
         * Returns supported css value for css property. Could be used to check support or get prefixed value string.
         * @param {string} prop Property
         * @param {string} value Value name
         * @returns {string|null}
         */
        getCssValue: function(prop, value) {
            // Create dummy element to test value
            var dummy = document.createElement('div');

            // Get supported property name
            var jsProperty = this.getJsProperty(prop);

            // Try unprefixed value
            if (this._valueSupported(jsProperty, value, dummy)) {
                return value;
            }

            var prefixedValue;

            // If we know the vendor already try prefixed value
            if (this._vendor) {
                prefixedValue = this._prefixValue(this._vendor, value);
                if (this._valueSupported(jsProperty, prefixedValue, dummy)) {
                    return prefixedValue;
                }
            }

            // Try all vendors
            for (var vendor in this._vendors) {
                prefixedValue = this._prefixValue(this._vendors[vendor], value);
                if (this._valueSupported(jsProperty, prefixedValue, dummy)) {
                    // Vendor detected. Cache it.
                    this._vendor = this._vendors[vendor];
                    return prefixedValue;
                }
            }
            // No support for value
            return null;
        }
    };

    var prefix = new Prefix();

    // We will need this frequently. Lets have it as a global until we encapsulate properly.
    var transformJsProperty = prefix.getJsProperty('transform');

    // Will hold if browser creates a positioning context for fixed elements.
    var fixedPositioningContext;

    // Checks if browser creates a positioning context for fixed elements.
    // Transform rule will create a positioning context on browsers who follow the spec.
    // Ie for example will fix it according to documentElement
    // TODO: Other css rules also effects. perspective creates at chrome but not in firefox. transform-style preserve3d effects.
    function checkFixedPositioningContextSupport() {
        var support = false;
        var parent = document.createElement('div');
        var child = document.createElement('div');
        parent.appendChild(child);
        parent.style[transformJsProperty] = 'translate(0)';
        // Make sure there is space on top of parent
        parent.style.marginTop = '10px';
        parent.style.visibility = 'hidden';
        child.style.position = 'fixed';
        child.style.top = 0;
        document.body.appendChild(parent);
        var rect = child.getBoundingClientRect();
        // If offset top is greater than 0 meand transformed element created a positioning context.
        if (rect.top > 0) {
            support = true;
        }
        // Remove dummy content
        document.body.removeChild(parent);
        return support;
    }

    // It will return null if position sticky is not supported
    var nativeStickyValue = prefix.getCssValue('position', 'sticky');

    // It will return null if position fixed is not supported
    var fixedPositionValue = prefix.getCssValue('position', 'fixed');

    // Dirty business
    var ie = navigator.appName === 'Microsoft Internet Explorer';
    var ieversion;

    if (ie) {
        ieversion = parseFloat(navigator.appVersion.split("MSIE")[1]);
    }

    function FixTo(child, parent, options) {
        this.child = child;
        this._$child = $(child);
        this.parent = parent;
        this.options = {
            className: 'fixto-fixed',
            top: 0
        };
        this._setOptions(options);
    }

    FixTo.prototype = {
        // Returns the total outerHeight of the elements passed to mind option. Will return 0 if none.
        _mindtop: function() {
            var top = 0;
            if (this._$mind) {
                var el;
                var rect;
                var height;
                for (var i = 0, l = this._$mind.length; i < l; i++) {
                    el = this._$mind[i];
                    rect = el.getBoundingClientRect();
                    if (rect.height) {
                        top += rect.height;
                    } else {
                        var styles = computedStyle.getAll(el);
                        top += el.offsetHeight + computedStyle.toFloat(styles.marginTop) + computedStyle.toFloat(styles.marginBottom);
                    }
                }
            }
            return top;
        },

        // Public method to stop the behaviour of this instance.
        stop: function() {
            this._stop();
            this._running = false;
        },

        // Public method starts the behaviour of this instance.
        start: function() {

            // Start only if it is not running not to attach event listeners multiple times.
            if (!this._running) {
                this._start();
                this._running = true;
            }
        },

        //Public method to destroy fixto behaviour
        destroy: function() {
            this.stop();

            this._destroy();

            // Remove jquery data from the element
            this._$child.removeData('fixto-instance');

            // set properties to null to break references
            for (var prop in this) {
                if (this.hasOwnProperty(prop)) {
                    this[prop] = null;
                }
            }
        },

        _setOptions: function(options) {
            $.extend(this.options, options);
            if (this.options.mind) {
                this._$mind = $(this.options.mind);
            }
            if (this.options.zIndex) {
                this.child.style.zIndex = this.options.zIndex;
            }
        },

        setOptions: function(options) {
            this._setOptions(options);
            this.refresh();
        },

        // Methods could be implemented by subclasses

        _stop: function() {

        },

        _start: function() {

        },

        _destroy: function() {

        },

        refresh: function() {

        }
    };

    // Class FixToContainer
    function FixToContainer(child, parent, options) {
        FixTo.call(this, child, parent, options);
        this._replacer = new mimicNode.MimicNode(child);
        this._ghostNode = this._replacer.replacer;

        this._saveStyles();

        this._saveViewportHeight();

        // Create anonymous functions and keep references to register and unregister events.
        this._proxied_onscroll = this._bind(this._onscroll, this);
        this._proxied_onresize = this._bind(this._onresize, this);

        this.start();
    }

    FixToContainer.prototype = new FixTo();

    $.extend(FixToContainer.prototype, {

        // Returns an anonymous function that will call the given function in the given context
        _bind: function(fn, context) {
            return function() {
                return fn.call(context);
            };
        },

        // at ie8 maybe only in vm window resize event fires everytime an element is resized.
        _toresize: ieversion === 8 ? document.documentElement : window,

        _onscroll: function _onscroll() {
            this._scrollTop = document.documentElement.scrollTop || document.body.scrollTop;
            this._parentBottom = (this.parent.offsetHeight + this._fullOffset('offsetTop', this.parent));

            if (this.options.mindBottomPadding !== false) {
                this._parentBottom -= computedStyle.getFloat(this.parent, 'paddingBottom');
            }

            if (!this.fixed) {

                var childStyles = computedStyle.getAll(this.child);

                if (
                    this._scrollTop < this._parentBottom &&
                    this._scrollTop > (this._fullOffset('offsetTop', this.child) - this.options.top - this._mindtop()) &&
                    this._viewportHeight > (this.child.offsetHeight + computedStyle.toFloat(childStyles.marginTop) + computedStyle.toFloat(childStyles.marginBottom))
                ) {

                    this._fix();
                    this._adjust();
                }
            } else {
                if (this._scrollTop > this._parentBottom || this._scrollTop < (this._fullOffset('offsetTop', this._ghostNode) - this.options.top - this._mindtop())) {
                    this._unfix();
                    return;
                }
                this._adjust();
            }
        },

        _adjust: function _adjust() {
            var top = 0;
            var mindTop = this._mindtop();
            var diff = 0;
            var childStyles = computedStyle.getAll(this.child);
            var context = null;

            if (fixedPositioningContext) {
                // Get positioning context.
                context = this._getContext();
                if (context) {
                    // There is a positioning context. Top should be according to the context.
                    top = Math.abs(context.getBoundingClientRect().top);
                }
            }

            diff = (this._parentBottom - this._scrollTop) - (this.child.offsetHeight + computedStyle.toFloat(childStyles.marginBottom) + mindTop + this.options.top);

            if (diff > 0) {
                diff = 0;
            }

            this.child.style.top = (diff + mindTop + top + this.options.top) - computedStyle.toFloat(childStyles.marginTop) + 'px';
        },

        // Calculate cumulative offset of the element.
        // Optionally according to context
        _fullOffset: function _fullOffset(offsetName, elm, context) {
            var offset = elm[offsetName];
            var offsetParent = elm.offsetParent;

            // Add offset of the ascendent tree until we reach to the document root or to the given context
            while (offsetParent !== null && offsetParent !== context) {
                offset = offset + offsetParent[offsetName];
                offsetParent = offsetParent.offsetParent;
            }

            return offset;
        },

        // Get positioning context of the element.
        // We know that the closest parent that a transform rule applied will create a positioning context.
        _getContext: function() {
            var parent;
            var element = this.child;
            var context = null;
            var styles;

            // Climb up the treee until reaching the context
            while (!context) {
                parent = element.parentNode;
                if (parent === document.documentElement) {
                    return null;
                }

                styles = computedStyle.getAll(parent);
                // Element has a transform rule
                if (styles[transformJsProperty] !== 'none') {
                    context = parent;
                    break;
                }
                element = parent;
            }
            return context;
        },

        _fix: function _fix() {
            var child = this.child;
            var childStyle = child.style;
            var childStyles = computedStyle.getAll(child);
            var left = child.getBoundingClientRect().left;
            var width = childStyles.width;

            this._saveStyles();

            if (document.documentElement.currentStyle) {
                // Function for ie<9. When hasLayout is not triggered in ie7, he will report currentStyle as auto, clientWidth as 0. Thus using offsetWidth.
                // Opera also falls here
                width = (child.offsetWidth) - (computedStyle.toFloat(childStyles.paddingLeft) + computedStyle.toFloat(childStyles.paddingRight) + computedStyle.toFloat(childStyles.borderLeftWidth) + computedStyle.toFloat(childStyles.borderRightWidth)) + 'px';
            }

            // Ie still fixes the container according to the viewport.
            if (fixedPositioningContext) {
                var context = this._getContext();
                if (context) {
                    // There is a positioning context. Left should be according to the context.
                    left = child.getBoundingClientRect().left - context.getBoundingClientRect().left;
                }
            }

            this._replacer.replace();

            childStyle.left = (left - computedStyle.toFloat(childStyles.marginLeft)) + 'px';
            childStyle.width = width;

            childStyle.position = 'fixed';
            childStyle.top = this._mindtop() + this.options.top - computedStyle.toFloat(childStyles.marginTop) + 'px';
            this._$child.addClass(this.options.className);
            this.fixed = true;
        },

        _unfix: function _unfix() {
            var childStyle = this.child.style;
            this._replacer.hide();
            childStyle.position = this._childOriginalPosition;
            childStyle.top = this._childOriginalTop;
            childStyle.width = this._childOriginalWidth;
            childStyle.left = this._childOriginalLeft;
            this._$child.removeClass(this.options.className);
            this.fixed = false;
        },

        _saveStyles: function() {
            var childStyle = this.child.style;
            this._childOriginalPosition = childStyle.position;
            this._childOriginalTop = childStyle.top;
            this._childOriginalWidth = childStyle.width;
            this._childOriginalLeft = childStyle.left;
        },

        _onresize: function() {
            this.refresh();
        },

        _saveViewportHeight: function() {
            // ie8 doesn't support innerHeight
            this._viewportHeight = window.innerHeight || document.documentElement.clientHeight;
        },

        _stop: function() {
            // Unfix the container immediately.
            this._unfix();
            // remove event listeners
            $(window).unbind('scroll', this._proxied_onscroll);
            $(this._toresize).unbind('resize', this._proxied_onresize);
        },

        _start: function() {
            // Trigger onscroll to have the effect immediately.
            this._onscroll();

            // Attach event listeners
            $(window).bind('scroll', this._proxied_onscroll);
            $(this._toresize).bind('resize', this._proxied_onresize);
        },

        _destroy: function() {
            // Destroy mimic node instance
            this._replacer.destroy();
        },

        refresh: function() {
            this._saveViewportHeight();
            this._unfix();
            this._onscroll();
        }
    });

    function NativeSticky(child, parent, options) {
        FixTo.call(this, child, parent, options);
        this.start();
    }

    NativeSticky.prototype = new FixTo();

    $.extend(NativeSticky.prototype, {
        _start: function() {

            var childStyles = computedStyle.getAll(this.child);

            this._childOriginalPosition = childStyles.position;
            this._childOriginalTop = childStyles.top;

            this.child.style.position = nativeStickyValue;
            this.refresh();
        },

        _stop: function() {
            this.child.style.position = this._childOriginalPosition;
            this.child.style.top = this._childOriginalTop;
        },

        refresh: function() {
            this.child.style.top = this._mindtop() + this.options.top + 'px';
        }
    });

    var fixTo = function fixTo(childElement, parentElement, options) {
        if ((nativeStickyValue && !options) || (nativeStickyValue && options && options.useNativeSticky !== false)) {
            // Position sticky supported and user did not disabled the usage of it.
            return new NativeSticky(childElement, parentElement, options);
        } else if (fixedPositionValue) {
            // Position fixed supported

            if (fixedPositioningContext === undefined) {
                // We don't know yet if browser creates fixed positioning contexts. Check it.
                fixedPositioningContext = checkFixedPositioningContextSupport();
            }

            return new FixToContainer(childElement, parentElement, options);
        } else {
            return 'Neither fixed nor sticky positioning supported';
        }
    };

    /*
    No support for ie lt 8
    */

    if (ieversion < 8) {
        fixTo = function() {
            return 'not supported';
        };
    }

    // Let it be a jQuery Plugin
    $.fn.fixTo = function(targetSelector, options) {

        var $targets = $(targetSelector);

        var i = 0;
        return this.each(function() {

            // Check the data of the element.
            var instance = $(this).data('fixto-instance');

            // If the element is not bound to an instance, create the instance and save it to elements data.
            if (!instance) {
                $(this).data('fixto-instance', fixTo(this, $targets[i], options));
            } else {
                // If we already have the instance here, expect that targetSelector parameter will be a string
                // equal to a public methods name. Run the method on the instance without checking if
                // it exists or it is a public method or not. Cause nasty errors when necessary.
                var method = targetSelector;
                instance[method].call(instance, options);
            }
            i++;
        });
    };

    /*
        Expose
    */

    return {
        FixToContainer: FixToContainer,
        fixTo: fixTo,
        computedStyle: computedStyle,
        mimicNode: mimicNode
    };

}(window.jQuery, window, document));

// @preserve jQuery.floatThead 1.2.13 - http://mkoryak.github.io/floatThead/ - Copyright (c) 2012 - 2015 Misha Koryak
// @license MIT

/* @author Misha Koryak
 * @projectDescription lock a table header in place while scrolling - without breaking styles or events bound to the header
 *
 * Dependencies:
 * jquery 1.9.0 + [required] OR jquery 1.7.0 + jquery UI core
 *
 * http://mkoryak.github.io/floatThead/
 *
 * Tested on FF13+, Chrome 21+, IE8, IE9, IE10, IE11
 *
 */
(function($) {
    /**
     * provides a default config object. You can modify this after including this script if you want to change the init defaults
     * @type {Object}
     */
    $.floatThead = $.floatThead || {};
    $.floatThead.defaults = {
        cellTag: null, // DEPRECATED - use headerCellSelector instead
        headerCellSelector: 'tr:visible:first>*:visible', //thead cells are this.
        zIndex: 1001, //zindex of the floating thead (actually a container div)
        debounceResizeMs: 10, //Deprecated!
        useAbsolutePositioning: null, //if set to NULL - defaults: has scrollContainer=true, doesn't have scrollContainer=false
        scrollingTop: 0, //String or function($table) - offset from top of window where the header should not pass above
        scrollingBottom: 0, //String or function($table) - offset from the bottom of the table where the header should stop scrolling
        scrollContainer: function($table) {
            return $([]); //if the table has horizontal scroll bars then this is the container that has overflow:auto and causes those scroll bars
        },
        getSizingRow: function($table, $cols, $fthCells) { // this is only called when using IE,
            // override it if the first row of the table is going to contain colgroups (any cell spans greater than one col)
            // it should return a jquery object containing a wrapped set of table cells comprising a row that contains no col spans and is visible
            return $table.find('tbody tr:visible:first>*:visible');
        },
        floatTableClass: 'floatThead-table',
        floatWrapperClass: 'floatThead-wrapper',
        floatContainerClass: 'floatThead-container',
        copyTableClass: true, //copy 'class' attribute from table into the floated table so that the styles match.
        enableAria: false, //will copy header text from the floated header back into the table for screen readers. Might cause the css styling to be off. beware!
        autoReflow: false, //(undocumented) - use MutationObserver api to reflow automatically when internal table DOM changes
        debug: false //print possible issues (that don't prevent script loading) to console, if console exists.
    };

    var util = window._;

    var canObserveMutations = typeof MutationObserver !== 'undefined';

    //browser stuff
    var ieVersion = function() {
        for (var a = 3, b = document.createElement("b"), c = b.all || []; a = 1 + a, b.innerHTML = "<!--[if gt IE " + a + "]><i><![endif]-->", c[0];);
        return 4 < a ? a : document.documentMode
    }();
    var isFF = /Gecko\//.test(navigator.userAgent);
    var isWebkit = /WebKit\//.test(navigator.userAgent);

    //safari 7 (and perhaps others) reports table width to be parent container's width if max-width is set on table. see: https://github.com/mkoryak/floatThead/issues/108
    var isTableWidthBug = function() {
        if (isWebkit) {
            var $test = $('<div style="width:0px"><table style="max-width:100%"><tr><th><div style="min-width:100px;">X</div></th></tr></table></div>');
            $("body").append($test);
            var ret = ($test.find("table").width() == 0);
            $test.remove();
            return ret;
        }
        return false;
    };

    var createElements = !isFF && !ieVersion; //FF can read width from <col> elements, but webkit cannot

    var $window = $(window);

    /**
     * @param debounceMs
     * @param cb
     */
    function windowResize(debounceMs, eventName, cb) {
        if (ieVersion == 8) { //ie8 is crap: https://github.com/mkoryak/floatThead/issues/65
            var winWidth = $window.width();
            var debouncedCb = util.debounce(function() {
                var winWidthNew = $window.width();
                if (winWidth != winWidthNew) {
                    winWidth = winWidthNew;
                    cb();
                }
            }, debounceMs);
            $window.on(eventName, debouncedCb);
        } else {
            $window.on(eventName, util.debounce(cb, debounceMs));
        }
    }

    function debug(str) {
        window && window.console && window.console.log && window.console.log("jQuery.floatThead: " + str);
    }

    //returns fractional pixel widths
    function getOffsetWidth(el) {
        var rect = el.getBoundingClientRect();
        return rect.width || rect.right - rect.left;
    }

    /**
     * try to calculate the scrollbar width for your browser/os
     * @return {Number}
     */
    function scrollbarWidth() {
        var $div = $( //borrowed from anti-scroll
            '<div style="width:50px;height:50px;overflow-y:scroll;' + 'position:absolute;top:-200px;left:-200px;"><div style="height:100px;width:100%">' + '</div>'
        );
        $('body').append($div);
        var w1 = $div.innerWidth();
        var w2 = $('div', $div).innerWidth();
        $div.remove();
        return w1 - w2;
    }
    /**
     * Check if a given table has been datatableized (http://datatables.net)
     * @param $table
     * @return {Boolean}
     */
    function isDatatable($table) {
        if ($table.dataTableSettings) {
            for (var i = 0; i < $table.dataTableSettings.length; i++) {
                var table = $table.dataTableSettings[i].nTable;
                if ($table[0] == table) {
                    return true;
                }
            }
        }
        return false;
    }

    function tableWidth($table, $fthCells, isOuter) {
        // see: https://github.com/mkoryak/floatThead/issues/108
        var fn = isOuter ? "outerWidth" : "width";
        if (isTableWidthBug && $table.css("max-width")) {
            var w = 0;
            if (isOuter) {
                w += parseInt($table.css("borderLeft"), 10);
                w += parseInt($table.css("borderRight"), 10);
            }
            for (var i = 0; i < $fthCells.length; i++) {
                w += $fthCells.get(i).offsetWidth;
            }
            return w;
        } else {
            return $table[fn]();
        }
    }
    $.fn.floatThead = function(map) {
        map = map || {};
        if (!util) { //may have been included after the script? lets try to grab it again.
            util = window._ || $.floatThead._;
            if (!util) {
                throw new Error("jquery.floatThead-slim.js requires underscore. You should use the non-lite version since you do not have underscore.");
            }
        }

        if (ieVersion < 8) {
            return this; //no more crappy browser support.
        }

        var mObs = null; //mutation observer lives in here if we can use it / make it

        if (util.isFunction(isTableWidthBug)) {
            isTableWidthBug = isTableWidthBug();
        }

        if (util.isString(map)) {
            var command = map;
            var ret = this;
            this.filter('table').each(function() {
                var $this = $(this);
                var opts = $this.data('floatThead-lazy');
                if (opts) {
                    $this.floatThead(opts);
                }
                var obj = $this.data('floatThead-attached');
                if (obj && util.isFunction(obj[command])) {
                    var r = obj[command]();
                    if (typeof r !== 'undefined') {
                        ret = r;
                    }
                }
            });
            return ret;
        }
        var opts = $.extend({}, $.floatThead.defaults || {}, map);

        $.each(map, function(key, val) {
            if ((!(key in $.floatThead.defaults)) && opts.debug) {
                debug("Used [" + key + "] key to init plugin, but that param is not an option for the plugin. Valid options are: " + (util.keys($.floatThead.defaults)).join(', '));
            }
        });
        if (opts.debug) {
            var v = $.fn.jquery.split(".");
            if (parseInt(v[0], 10) == 1 && parseInt(v[1], 10) <= 7) {
                debug("jQuery version " + $.fn.jquery + " detected! This plugin supports 1.8 or better, or 1.7.x with jQuery UI 1.8.24 -> http://jqueryui.com/resources/download/jquery-ui-1.8.24.zip")
            }
        }

        this.filter(':not(.' + opts.floatTableClass + ')').each(function() {
            var floatTheadId = util.uniqueId();
            var $table = $(this);
            if ($table.data('floatThead-attached')) {
                return true; //continue the each loop
            }
            if (!$table.is('table')) {
                throw new Error('jQuery.floatThead must be run on a table element. ex: $("table").floatThead();');
            }
            canObserveMutations = opts.autoReflow && canObserveMutations; //option defaults to false!
            var $header = $table.children('thead:first');
            var $tbody = $table.children('tbody:first');
            if ($header.length == 0 || $tbody.length == 0) {
                $table.data('floatThead-lazy', opts);
                $table.unbind("reflow").one('reflow', function() {
                    $table.floatThead(opts);
                });
                return;
            }
            if ($table.data('floatThead-lazy')) {
                $table.unbind("reflow");
            }
            $table.data('floatThead-lazy', false);

            var headerFloated = false;
            var scrollingTop, scrollingBottom;
            var scrollbarOffset = {
                vertical: 0,
                horizontal: 0
            };
            var scWidth = scrollbarWidth();
            var lastColumnCount = 0; //used by columnNum()
            var $scrollContainer = opts.scrollContainer($table) || $([]); //guard against returned nulls
            var locked = $scrollContainer.length > 0;

            var useAbsolutePositioning = opts.useAbsolutePositioning;
            if (useAbsolutePositioning == null) { //defaults: locked=true, !locked=false
                useAbsolutePositioning = locked;
            }
            if (!useAbsolutePositioning) {
                headerFloated = true; //#127
            }
            var $caption = $table.find("caption");
            var haveCaption = $caption.length == 1;
            if (haveCaption) {
                var captionAlignTop = ($caption.css("caption-side") || $caption.attr("align") || "top") === "top";
            }

            var $fthGrp = $('<fthfoot style="display:table-footer-group;border-spacing:0;height:0;border-collapse:collapse;"/>');

            var wrappedContainer = false; //used with absolute positioning enabled. did we need to wrap the scrollContainer/table with a relative div?
            var $wrapper = $([]); //used when absolute positioning enabled - wraps the table and the float container
            var absoluteToFixedOnScroll = ieVersion <= 9 && !locked && useAbsolutePositioning; //on IE using absolute positioning doesn't look good with window scrolling, so we change position to fixed on scroll, and then change it back to absolute when done.
            var $floatTable = $("<table/>");
            var $floatColGroup = $("<colgroup/>");
            var $tableColGroup = $table.children('colgroup:first');
            var existingColGroup = true;
            if ($tableColGroup.length == 0) {
                $tableColGroup = $("<colgroup/>");
                existingColGroup = false;
            }
            var $fthRow = $('<fthtr style="display:table-row;border-spacing:0;height:0;border-collapse:collapse"/>'); //created unstyled elements (used for sizing the table because chrome can't read <col> width)
            var $floatContainer = $('<div style="overflow: hidden;" aria-hidden="true" class="floatThead-floatContainer"></div>');
            var floatTableHidden = false; //this happens when the table is hidden and we do magic when making it visible
            var $newHeader = $("<thead/>");
            var $sizerRow = $('<tr class="size-row"/>');
            var $sizerCells = $([]);
            var $tableCells = $([]); //used for sizing - either $sizerCells or $tableColGroup cols. $tableColGroup cols are only created in chrome for borderCollapse:collapse because of a chrome bug.
            var $headerCells = $([]);
            var $fthCells = $([]); //created elements

            $newHeader.append($sizerRow);
            $table.prepend($tableColGroup);
            if (createElements) {
                $fthGrp.append($fthRow);
                $table.append($fthGrp);
            }

            $floatTable.append($floatColGroup);
            $floatContainer.append($floatTable);
            if (opts.copyTableClass) {
                $floatTable.attr('class', $table.attr('class'));
            }
            $floatTable.attr({ //copy over some deprecated table attributes that people still like to use. Good thing people don't use colgroups...
                'cellpadding': $table.attr('cellpadding'),
                'cellspacing': $table.attr('cellspacing'),
                'border': $table.attr('border')
            });
            var tableDisplayCss = $table.css('display');
            $floatTable.css({
                'borderCollapse': $table.css('borderCollapse'),
                'border': $table.css('border'),
                'display': tableDisplayCss
            });
            if (tableDisplayCss == 'none') {
                floatTableHidden = true;
            }

            $floatTable.addClass(opts.floatTableClass).css({
                'margin': 0,
                'border-bottom-width': 0
            }); //must have no margins or you won't be able to click on things under floating table

            if (useAbsolutePositioning) {
                var makeRelative = function($container, alwaysWrap) {
                    var positionCss = $container.css('position');
                    var relativeToScrollContainer = (positionCss == "relative" || positionCss == "absolute");
                    if (!relativeToScrollContainer || alwaysWrap) {
                        var css = {
                            "paddingLeft": $container.css('paddingLeft'),
                            "paddingRight": $container.css('paddingRight')
                        };
                        $floatContainer.css(css);
                        $container = $container.wrap("<div class='" + opts.floatWrapperClass + "' style='position: relative; clear:both;'></div>").parent();
                        wrappedContainer = true;
                    }
                    return $container;
                };
                if (locked) {
                    $wrapper = makeRelative($scrollContainer, true);
                    $wrapper.append($floatContainer);
                } else {
                    $wrapper = makeRelative($table);
                    $table.after($floatContainer);
                }
            } else {
                $table.after($floatContainer);
            }

            $floatContainer.css({
                position: useAbsolutePositioning ? 'absolute' : 'fixed',
                marginTop: 0,
                top: useAbsolutePositioning ? 0 : 'auto',
                zIndex: opts.zIndex
            });
            $floatContainer.addClass(opts.floatContainerClass);
            updateScrollingOffsets();

            var layoutFixed = {
                'table-layout': 'fixed'
            };
            var layoutAuto = {
                'table-layout': $table.css('tableLayout') || 'auto'
            };
            var originalTableWidth = $table[0].style.width || ""; //setting this to auto is bad: #70
            var originalTableMinWidth = $table.css('minWidth') || "";

            function eventName(name) {
                return name + '.fth-' + floatTheadId + '.floatTHead'
            }

            function setHeaderHeight() {
                var headerHeight = 0;
                $header.children("tr:visible").each(function() {
                    headerHeight += $(this).outerHeight(true);
                });
                if ($table.css('border-collapse') == 'collapse') {
                    var tableBorderTopHeight = parseInt($table.css('border-top-width'), 10);
                    var cellBorderTopHeight = parseInt($table.find("thead tr:first").find(">*:first").css('border-top-width'), 10);
                    if (tableBorderTopHeight > cellBorderTopHeight) {
                        headerHeight -= (tableBorderTopHeight / 2); //id love to see some docs where this magic recipe is found..
                    }
                }
                $sizerRow.outerHeight(headerHeight);
                $sizerCells.outerHeight(headerHeight);
            }

            function setFloatWidth() {
                var tw = tableWidth($table, $fthCells, true);
                var width = $scrollContainer.width() || tw;
                var floatContainerWidth = $scrollContainer.css("overflow-y") != 'hidden' ? width - scrollbarOffset.vertical : width;
                $floatContainer.width(floatContainerWidth);
                if (locked) {
                    var percent = 100 * tw / (floatContainerWidth);
                    $floatTable.css('width', percent + '%');
                } else {
                    $floatTable.outerWidth(tw);
                }
            }

            function updateScrollingOffsets() {
                scrollingTop = (util.isFunction(opts.scrollingTop) ? opts.scrollingTop($table) : opts.scrollingTop) || 0;
                scrollingBottom = (util.isFunction(opts.scrollingBottom) ? opts.scrollingBottom($table) : opts.scrollingBottom) || 0;
            }

            /**
             * get the number of columns and also rebuild resizer rows if the count is different than the last count
             */
            function columnNum() {
                var count, $headerColumns;
                if (existingColGroup) {
                    count = $tableColGroup.find('col').length;
                } else {
                    var selector;
                    if (opts.cellTag == null && opts.headerCellSelector) { //TODO: once cellTag option is removed, remove this conditional
                        selector = opts.headerCellSelector;
                    } else {
                        selector = 'tr:first>' + opts.cellTag;
                    }
                    if (util.isNumber(selector)) {
                        //it's actually a row count. (undocumented, might be removed!)
                        return selector;
                    }
                    $headerColumns = $header.find(selector);
                    count = 0;
                    $headerColumns.each(function() {
                        count += parseInt(($(this).attr('colspan') || 1), 10);
                    });
                }
                if (count != lastColumnCount) {
                    lastColumnCount = count;
                    var cells = [],
                        cols = [],
                        psuedo = [],
                        content;
                    for (var x = 0; x < count; x++) {
                        if (opts.enableAria && (content = $headerColumns.eq(x).text())) {
                            cells.push('<th scope="col" class="floatThead-col">' + content + '</th>');
                        } else {
                            cells.push('<th class="floatThead-col"/>');
                        }
                        cols.push('<col/>');
                        psuedo.push("<fthtd style='display:table-cell;height:0;width:auto;'/>");
                    }

                    cols = cols.join('');
                    cells = cells.join('');

                    if (createElements) {
                        psuedo = psuedo.join('');
                        $fthRow.html(psuedo);
                        $fthCells = $fthRow.find('fthtd');
                    }

                    $sizerRow.html(cells);
                    $sizerCells = $sizerRow.find("th");
                    if (!existingColGroup) {
                        $tableColGroup.html(cols);
                    }
                    $tableCells = $tableColGroup.find('col');
                    $floatColGroup.html(cols);
                    $headerCells = $floatColGroup.find("col");

                }
                return count;
            }

            function refloat() { //make the thing float
                if (!headerFloated) {
                    headerFloated = true;
                    if (useAbsolutePositioning) { //#53, #56
                        var tw = tableWidth($table, $fthCells, true);
                        var wrapperWidth = $wrapper.width();
                        if (tw > wrapperWidth) {
                            $table.css('minWidth', tw);
                        }
                    }
                    $table.css(layoutFixed);
                    $floatTable.css(layoutFixed);
                    $floatTable.append($header); //append because colgroup must go first in chrome
                    $tbody.before($newHeader);
                    setHeaderHeight();
                }
            }

            function unfloat() { //put the header back into the table
                if (headerFloated) {
                    headerFloated = false;
                    if (useAbsolutePositioning) { //#53, #56
                        $table.width(originalTableWidth);
                    }
                    $newHeader.detach();
                    $table.prepend($header);
                    $table.css(layoutAuto);
                    $floatTable.css(layoutAuto);
                    $table.css('minWidth', originalTableMinWidth); //this looks weird, but it's not a bug. Think about it!!
                    $table.css('minWidth', tableWidth($table, $fthCells)); //#121
                }
            }
            var isHeaderFloatingLogical = false; //for the purpose of this event, the header is/isnt floating, even though the element
            //might be in some other state. this is what the header looks like to the user
            function triggerFloatEvent(isFloating) {
                if (isHeaderFloatingLogical != isFloating) {
                    isHeaderFloatingLogical = isFloating;
                    $table.triggerHandler("floatThead", [isFloating, $floatContainer])
                }
            }

            function changePositioning(isAbsolute) {
                if (useAbsolutePositioning != isAbsolute) {
                    useAbsolutePositioning = isAbsolute;
                    $floatContainer.css({
                        position: useAbsolutePositioning ? 'absolute' : 'fixed'
                    });
                }
            }

            function getSizingRow($table, $cols, $fthCells, ieVersion) {
                if (createElements) {
                    return $fthCells;
                } else if (ieVersion) {
                    return opts.getSizingRow($table, $cols, $fthCells);
                } else {
                    return $cols;
                }
            }

            /**
             * returns a function that updates the floating header's cell widths.
             * @return {Function}
             */
            function reflow() {
                var i;
                var numCols = columnNum(); //if the tables columns changed dynamically since last time (datatables), rebuild the sizer rows and get a new count

                return function() {
                    $tableCells = $tableColGroup.find('col');
                    var $rowCells = getSizingRow($table, $tableCells, $fthCells, ieVersion);

                    if ($rowCells.length == numCols && numCols > 0) {
                        if (!existingColGroup) {
                            for (i = 0; i < numCols; i++) {
                                $tableCells.eq(i).css('width', '');
                            }
                        }
                        unfloat();
                        var widths = [];
                        for (i = 0; i < numCols; i++) {
                            widths[i] = getOffsetWidth($rowCells.get(i));
                        }
                        for (i = 0; i < numCols; i++) {
                            $headerCells.eq(i).width(widths[i]);
                            $tableCells.eq(i).width(widths[i]);
                        }
                        refloat();
                    } else {
                        $floatTable.append($header);
                        $table.css(layoutAuto);
                        $floatTable.css(layoutAuto);
                        setHeaderHeight();
                    }
                };
            }

            function floatContainerBorderWidth(side) {
                var border = $scrollContainer.css("border-" + side + "-width");
                var w = 0;
                if (border && ~border.indexOf('px')) {
                    w = parseInt(border, 10);
                }
                return w;
            }
            /**
             * first performs initial calculations that we expect to not change when the table, window, or scrolling container are scrolled.
             * returns a function that calculates the floating container's top and left coords. takes into account if we are using page scrolling or inner scrolling
             * @return {Function}
             */
            function calculateFloatContainerPosFn() {
                var scrollingContainerTop = $scrollContainer.scrollTop();

                //this floatEnd calc was moved out of the returned function because we assume the table height doesn't change (otherwise we must reinit by calling calculateFloatContainerPosFn)
                var floatEnd;
                var tableContainerGap = 0;
                var captionHeight = haveCaption ? $caption.outerHeight(true) : 0;
                var captionScrollOffset = captionAlignTop ? captionHeight : -captionHeight;

                var floatContainerHeight = $floatContainer.height();
                var tableOffset = $table.offset();
                var tableLeftGap = 0; //can be caused by border on container (only in locked mode)
                var tableTopGap = 0;
                if (locked) {
                    var containerOffset = $scrollContainer.offset();
                    tableContainerGap = tableOffset.top - containerOffset.top + scrollingContainerTop;
                    if (haveCaption && captionAlignTop) {
                        tableContainerGap += captionHeight;
                    }
                    tableLeftGap = floatContainerBorderWidth('left');
                    tableTopGap = floatContainerBorderWidth('top');
                    tableContainerGap -= tableTopGap;
                } else {
                    floatEnd = tableOffset.top - scrollingTop - floatContainerHeight + scrollingBottom + scrollbarOffset.horizontal;
                }
                var windowTop = $window.scrollTop();
                var windowLeft = $window.scrollLeft();
                var scrollContainerLeft = $scrollContainer.scrollLeft();

                return function(eventType) {
                    var isTableHidden = $table[0].offsetWidth <= 0 && $table[0].offsetHeight <= 0;
                    if (!isTableHidden && floatTableHidden) {
                        floatTableHidden = false;
                        setTimeout(function() {
                            $table.triggerHandler("reflow");
                        }, 1);
                        return null;
                    }
                    if (isTableHidden) { //it's hidden
                        floatTableHidden = true;
                        if (!useAbsolutePositioning) {
                            return null;
                        }
                    }

                    if (eventType == 'windowScroll') {
                        windowTop = $window.scrollTop();
                        windowLeft = $window.scrollLeft();
                    } else if (eventType == 'containerScroll') {
                        scrollingContainerTop = $scrollContainer.scrollTop();
                        scrollContainerLeft = $scrollContainer.scrollLeft();
                    } else if (eventType != 'init') {
                        windowTop = $window.scrollTop();
                        windowLeft = $window.scrollLeft();
                        scrollingContainerTop = $scrollContainer.scrollTop();
                        scrollContainerLeft = $scrollContainer.scrollLeft();
                    }
                    if (isWebkit && (windowTop < 0 || windowLeft < 0)) { //chrome overscroll effect at the top of the page - breaks fixed positioned floated headers
                        return;
                    }

                    if (absoluteToFixedOnScroll) {
                        if (eventType == 'windowScrollDone') {
                            changePositioning(true); //change to absolute
                        } else {
                            changePositioning(false); //change to fixed
                        }
                    } else if (eventType == 'windowScrollDone') {
                        return null; //event is fired when they stop scrolling. ignore it if not 'absoluteToFixedOnScroll'
                    }

                    tableOffset = $table.offset();
                    if (haveCaption && captionAlignTop) {
                        tableOffset.top += captionHeight;
                    }
                    var top, left;
                    var tableHeight = $table.outerHeight();

                    if (locked && useAbsolutePositioning) { //inner scrolling, absolute positioning
                        if (tableContainerGap >= scrollingContainerTop) {
                            var gap = tableContainerGap - scrollingContainerTop + tableTopGap;
                            top = gap > 0 ? gap : 0;
                            triggerFloatEvent(false);
                        } else {
                            top = wrappedContainer ? tableTopGap : scrollingContainerTop;
                            //headers stop at the top of the viewport
                            triggerFloatEvent(true);
                        }
                        left = tableLeftGap;
                    } else if (!locked && useAbsolutePositioning) { //window scrolling, absolute positioning
                        if (windowTop > floatEnd + tableHeight + captionScrollOffset) {
                            top = tableHeight - floatContainerHeight + captionScrollOffset; //scrolled past table
                        } else if (tableOffset.top >= windowTop + scrollingTop) {
                            top = 0; //scrolling to table
                            unfloat();
                            triggerFloatEvent(false);
                        } else {
                            top = scrollingTop + windowTop - tableOffset.top + tableContainerGap + (captionAlignTop ? captionHeight : 0);
                            refloat(); //scrolling within table. header floated
                            triggerFloatEvent(true);
                        }
                        left = 0;
                    } else if (locked && !useAbsolutePositioning) { //inner scrolling, fixed positioning
                        if (tableContainerGap > scrollingContainerTop || scrollingContainerTop - tableContainerGap > tableHeight) {
                            top = tableOffset.top - windowTop;
                            unfloat();
                            triggerFloatEvent(false);
                        } else {
                            top = tableOffset.top + scrollingContainerTop - windowTop - tableContainerGap;
                            refloat();
                            triggerFloatEvent(true);
                            //headers stop at the top of the viewport
                        }
                        left = tableOffset.left + scrollContainerLeft - windowLeft;
                    } else if (!locked && !useAbsolutePositioning) { //window scrolling, fixed positioning
                        if (windowTop > floatEnd + tableHeight + captionScrollOffset) {
                            top = tableHeight + scrollingTop - windowTop + floatEnd + captionScrollOffset;
                            //scrolled past the bottom of the table
                        } else if (tableOffset.top > windowTop + scrollingTop) {
                            top = tableOffset.top - windowTop;
                            refloat();
                            triggerFloatEvent(false); //this is a weird case, the header never gets unfloated and i have no no way to know
                            //scrolled past the top of the table
                        } else {
                            //scrolling within the table
                            top = scrollingTop;
                            triggerFloatEvent(true);
                        }
                        left = tableOffset.left - windowLeft;
                    }
                    return {
                        top: top,
                        left: left
                    };
                };
            }
            /**
             * returns a function that caches old floating container position and only updates css when the position changes
             * @return {Function}
             */
            function repositionFloatContainerFn() {
                var oldTop = null;
                var oldLeft = null;
                var oldScrollLeft = null;
                return function(pos, setWidth, setHeight) {
                    if (pos != null && (oldTop != pos.top || oldLeft != pos.left)) {
                        $floatContainer.css({
                            top: pos.top,
                            left: pos.left
                        });
                        oldTop = pos.top;
                        oldLeft = pos.left;
                    }
                    if (setWidth) {
                        setFloatWidth();
                    }
                    if (setHeight) {
                        setHeaderHeight();
                    }
                    var scrollLeft = $scrollContainer.scrollLeft();
                    if (!useAbsolutePositioning || oldScrollLeft != scrollLeft) {
                        $floatContainer.scrollLeft(scrollLeft);
                        oldScrollLeft = scrollLeft;
                    }
                }
            }

            /**
             * checks if THIS table has scrollbars, and finds their widths
             */
            function calculateScrollBarSize() { //this should happen after the floating table has been positioned
                if ($scrollContainer.length) {
                    if ($scrollContainer.data().perfectScrollbar) {
                        scrollbarOffset = {
                            horizontal: 0,
                            vertical: 0
                        };
                    } else {
                        var sw = $scrollContainer.width(),
                            sh = $scrollContainer.height(),
                            th = $table.height(),
                            tw = tableWidth($table, $fthCells);
                        var offseth = sw < tw ? scWidth : 0;
                        var offsetv = sh < th ? scWidth : 0;
                        scrollbarOffset.horizontal = sw - offsetv < tw ? scWidth : 0;
                        scrollbarOffset.vertical = sh - offseth < th ? scWidth : 0;
                    }
                }
            }
            //finish up. create all calculation functions and bind them to events
            calculateScrollBarSize();

            var flow;

            var ensureReflow = function() {
                flow = reflow();
                flow();
            };

            ensureReflow();

            var calculateFloatContainerPos = calculateFloatContainerPosFn();
            var repositionFloatContainer = repositionFloatContainerFn();

            repositionFloatContainer(calculateFloatContainerPos('init'), true); //this must come after reflow because reflow changes scrollLeft back to 0 when it rips out the thead

            var windowScrollDoneEvent = util.debounce(function() {
                repositionFloatContainer(calculateFloatContainerPos('windowScrollDone'), false);
            }, 1);

            var windowScrollEvent = function() {
                repositionFloatContainer(calculateFloatContainerPos('windowScroll'), false);
                if (absoluteToFixedOnScroll) {
                    windowScrollDoneEvent();
                }
            };
            var containerScrollEvent = function() {
                repositionFloatContainer(calculateFloatContainerPos('containerScroll'), false);
            };

            var windowResizeEvent = function() {
                if ($table.is(":hidden")) {
                    return;
                }
                updateScrollingOffsets();
                calculateScrollBarSize();
                ensureReflow();
                calculateFloatContainerPos = calculateFloatContainerPosFn();
                repositionFloatContainer = repositionFloatContainerFn();
                repositionFloatContainer(calculateFloatContainerPos('resize'), true, true);
            };
            var reflowEvent = util.debounce(function() {
                if ($table.is(":hidden")) {
                    return;
                }
                calculateScrollBarSize();
                updateScrollingOffsets();
                ensureReflow();
                calculateFloatContainerPos = calculateFloatContainerPosFn();
                repositionFloatContainer(calculateFloatContainerPos('reflow'), true);
            }, 1);
            if (locked) { //internal scrolling
                if (useAbsolutePositioning) {
                    $scrollContainer.on(eventName('scroll'), containerScrollEvent);
                } else {
                    $scrollContainer.on(eventName('scroll'), containerScrollEvent);
                    $window.on(eventName('scroll'), windowScrollEvent);
                }
            } else { //window scrolling
                $window.on(eventName('scroll'), windowScrollEvent);
            }

            $window.on(eventName('load'), reflowEvent); //for tables with images

            windowResize(opts.debounceResizeMs, eventName('resize'), windowResizeEvent);
            $table.on('reflow', reflowEvent);
            if (isDatatable($table)) {
                $table
                    .on('filter', reflowEvent)
                    .on('sort', reflowEvent)
                    .on('page', reflowEvent);
            }

            $window.on(eventName('shown.bs.tab'), reflowEvent); // people cant seem to figure out how to use this plugin with bs3 tabs... so this :P
            $window.on(eventName('tabsactivate'), reflowEvent); // same thing for jqueryui

            if (canObserveMutations) {
                var mutationElement = null;
                if (_.isFunction(opts.autoReflow)) {
                    mutationElement = opts.autoReflow($table, $scrollContainer)
                }
                if (!mutationElement) {
                    mutationElement = $scrollContainer.length ? $scrollContainer[0] : $table[0]
                }
                mObs = new MutationObserver(function(e) {
                    var wasTableRelated = function(nodes) {
                        return nodes && nodes[0] && (nodes[0].nodeName == "THEAD" || nodes[0].nodeName == "TD" || nodes[0].nodeName == "TH");
                    };
                    for (var i = 0; i < e.length; i++) {
                        if (!(wasTableRelated(e[i].addedNodes) || wasTableRelated(e[i].removedNodes))) {
                            reflowEvent();
                            break;
                        }
                    }
                });
                mObs.observe(mutationElement, {
                    childList: true,
                    subtree: true
                });
            }

            //attach some useful functions to the table.
            $table.data('floatThead-attached', {
                destroy: function() {
                    var ns = '.fth-' + floatTheadId;
                    unfloat();
                    $table.css(layoutAuto);
                    $tableColGroup.remove();
                    createElements && $fthGrp.remove();
                    if ($newHeader.parent().length) { //only if it's in the DOM
                        $newHeader.replaceWith($header);
                    }
                    if (canObserveMutations) {
                        mObs.disconnect();
                        mObs = null;
                    }
                    $table.off('reflow');
                    $scrollContainer.off(ns);
                    if (wrappedContainer) {
                        if ($scrollContainer.length) {
                            $scrollContainer.unwrap();
                        } else {
                            $table.unwrap();
                        }
                    }
                    $table.css('minWidth', originalTableMinWidth);
                    $floatContainer.remove();
                    $table.data('floatThead-attached', false);
                    $window.off(ns);
                },
                reflow: function() {
                    reflowEvent();
                },
                setHeaderHeight: function() {
                    setHeaderHeight();
                },
                getFloatContainer: function() {
                    return $floatContainer;
                },
                getRowGroups: function() {
                    if (headerFloated) {
                        return $floatContainer.children("thead").add($table.children("tbody,tfoot"));
                    } else {
                        return $table.children("thead,tbody,tfoot");
                    }
                }
            });
        });
        return this;
    };
})(jQuery);
/* jQuery.floatThead.utils - http://mkoryak.github.io/floatThead/ - Copyright (c) 2012 - 2014 Misha Koryak
 * License: MIT
 *
 * This file is required if you do not use underscore in your project and you want to use floatThead.
 * It contains functions from underscore that the plugin uses.
 *
 * YOU DON'T NEED TO INCLUDE THIS IF YOU ALREADY INCLUDE UNDERSCORE!
 *
 */

(function($) {

    $.floatThead = $.floatThead || {};

    $.floatThead._ = window._ || (function() {
        var that = {};
        var hasOwnProperty = Object.prototype.hasOwnProperty,
            isThings = ['Arguments', 'Function', 'String', 'Number', 'Date', 'RegExp'];
        that.has = function(obj, key) {
            return hasOwnProperty.call(obj, key);
        };
        that.keys = function(obj) {
            if (obj !== Object(obj)) throw new TypeError('Invalid object');
            var keys = [];
            for (var key in obj)
                if (that.has(obj, key)) keys.push(key);
            return keys;
        };
        var idCounter = 0;
        that.uniqueId = function(prefix) {
            var id = ++idCounter + '';
            return prefix ? prefix + id : id;
        };
        $.each(isThings, function() {
            var name = this;
            that['is' + name] = function(obj) {
                return Object.prototype.toString.call(obj) == '[object ' + name + ']';
            };
        });
        that.debounce = function(func, wait, immediate) {
            var timeout, args, context, timestamp, result;
            return function() {
                context = this;
                args = arguments;
                timestamp = new Date();
                var later = function() {
                    var last = (new Date()) - timestamp;
                    if (last < wait) {
                        timeout = setTimeout(later, wait - last);
                    } else {
                        timeout = null;
                        if (!immediate) result = func.apply(context, args);
                    }
                };
                var callNow = immediate && !timeout;
                if (!timeout) {
                    timeout = setTimeout(later, wait);
                }
                if (callNow) result = func.apply(context, args);
                return result;
            };
        };
        return that;
    })();
})(jQuery);

/*!
 * jQuery Raty - A Star Rating Plugin
 *
 * The MIT License
 *
 * @author  : Washington Botelho
 * @doc     : http://wbotelhos.com/raty
 * @version : 2.7.0
 *
 */

;
(function($) {
  'use strict';

  var methods = {
    init: function(options) {
      return this.each(function() {
        this.self = $(this);

        methods.destroy.call(this.self);

        this.opt = $.extend(true, {}, $.fn.raty.defaults, options);

        methods._adjustCallback.call(this);
        methods._adjustNumber.call(this);
        methods._adjustHints.call(this);

        this.opt.score = methods._adjustedScore.call(this, this.opt.score);

        if (this.opt.starType !== 'img') {
          methods._adjustStarType.call(this);
        }

        methods._adjustPath.call(this);
        methods._createStars.call(this);

        if (this.opt.cancel) {
          methods._createCancel.call(this);
        }

        if (this.opt.precision) {
          methods._adjustPrecision.call(this);
        }

        methods._createScore.call(this);
        methods._apply.call(this, this.opt.score);
        methods._setTitle.call(this, this.opt.score);
        methods._target.call(this, this.opt.score);

        if (this.opt.readOnly) {
          methods._lock.call(this);
        } else {
          this.style.cursor = 'pointer';

          methods._binds.call(this);
        }
      });
    },

    _adjustCallback: function() {
      var options = ['number', 'readOnly', 'score', 'scoreName', 'target'];

      for (var i = 0; i < options.length; i++) {
        if (typeof this.opt[options[i]] === 'function') {
          this.opt[options[i]] = this.opt[options[i]].call(this);
        }
      }
    },

    _adjustedScore: function(score) {
      if (!score) {
        return score;
      }

      return methods._between(score, 0, this.opt.number);
    },

    _adjustHints: function() {
      if (!this.opt.hints) {
        this.opt.hints = [];
      }

      if (!this.opt.halfShow && !this.opt.half) {
        return;
      }

      var steps = this.opt.precision ? 10 : 2;

      for (var i = 0; i < this.opt.number; i++) {
        var group = this.opt.hints[i];

        if (Object.prototype.toString.call(group) !== '[object Array]') {
          group = [group];
        }

        this.opt.hints[i] = [];

        for (var j = 0; j < steps; j++) {
          var
            hint = group[j],
            last = group[group.length - 1];

          if (last === undefined) {
            last = null;
          }

          this.opt.hints[i][j] = hint === undefined ? last : hint;
        }
      }
    },

    _adjustNumber: function() {
      this.opt.number = methods._between(this.opt.number, 1, this.opt.numberMax);
    },

    _adjustPath: function() {
      this.opt.path = this.opt.path || '';

      if (this.opt.path && this.opt.path.charAt(this.opt.path.length - 1) !== '/') {
        this.opt.path += '/';
      }
    },

    _adjustPrecision: function() {
      this.opt.half = true;
    },

    _adjustStarType: function() {
      var replaces = ['cancelOff', 'cancelOn', 'starHalf', 'starOff', 'starOn'];

      this.opt.path = '';

      for (var i = 0; i < replaces.length; i++) {
        this.opt[replaces[i]] = this.opt[replaces[i]].replace('.', '-');
      }
    },

    _apply: function(score) {
      methods._fill.call(this, score);

      if (score) {
        if (score > 0) {
          this.score.val(score);
        }

        methods._roundStars.call(this, score);
      }
    },

    _between: function(value, min, max) {
      return Math.min(Math.max(parseFloat(value), min), max);
    },

    _binds: function() {
      if (this.cancel) {
        methods._bindOverCancel.call(this);
        methods._bindClickCancel.call(this);
        methods._bindOutCancel.call(this);
      }

      methods._bindOver.call(this);
      methods._bindClick.call(this);
      methods._bindOut.call(this);
    },

    _bindClick: function() {
      var that = this;

      that.stars.on('click.raty', function(evt) {
        var
          execute = true,
          score   = (that.opt.half || that.opt.precision) ? that.self.data('score') : (this.alt || $(this).data('alt'));

        if (that.opt.click) {
          execute = that.opt.click.call(that, +score, evt);
        }

        if (execute || execute === undefined) {
          if (that.opt.half && !that.opt.precision) {
            score = methods._roundHalfScore.call(that, score);
          }

          methods._apply.call(that, score);
        }
      });
    },

    _bindClickCancel: function() {
      var that = this;

      that.cancel.on('click.raty', function(evt) {
        that.score.removeAttr('value');

        if (that.opt.click) {
          that.opt.click.call(that, null, evt);
        }
      });
    },

    _bindOut: function() {
      var that = this;

      that.self.on('mouseleave.raty', function(evt) {
        var score = +that.score.val() || undefined;

        methods._apply.call(that, score);
        methods._target.call(that, score, evt);
        methods._resetTitle.call(that);

        if (that.opt.mouseout) {
          that.opt.mouseout.call(that, score, evt);
        }
      });
    },

    _bindOutCancel: function() {
      var that = this;

      that.cancel.on('mouseleave.raty', function(evt) {
        var icon = that.opt.cancelOff;

        if (that.opt.starType !== 'img') {
          icon = that.opt.cancelClass + ' ' + icon;
        }

        methods._setIcon.call(that, this, icon);

        if (that.opt.mouseout) {
          var score = +that.score.val() || undefined;

          that.opt.mouseout.call(that, score, evt);
        }
      });
    },

    _bindOver: function() {
      var that   = this,
          action = that.opt.half ? 'mousemove.raty' : 'mouseover.raty';

      that.stars.on(action, function(evt) {
        var score = methods._getScoreByPosition.call(that, evt, this);

        methods._fill.call(that, score);

        if (that.opt.half) {
          methods._roundStars.call(that, score, evt);
          methods._setTitle.call(that, score, evt);

          that.self.data('score', score);
        }

        methods._target.call(that, score, evt);

        if (that.opt.mouseover) {
          that.opt.mouseover.call(that, score, evt);
        }
      });
    },

    _bindOverCancel: function() {
      var that = this;

      that.cancel.on('mouseover.raty', function(evt) {
        var
          starOff = that.opt.path + that.opt.starOff,
          icon    = that.opt.cancelOn;

        if (that.opt.starType === 'img') {
          that.stars.attr('src', starOff);
        } else {
          icon = that.opt.cancelClass + ' ' + icon;

          that.stars.attr('class', starOff);
        }

        methods._setIcon.call(that, this, icon);
        methods._target.call(that, null, evt);

        if (that.opt.mouseover) {
          that.opt.mouseover.call(that, null);
        }
      });
    },

    _buildScoreField: function() {
      return $('<input />', { name: this.opt.scoreName, type: 'hidden' }).appendTo(this);
    },

    _createCancel: function() {
      var icon   = this.opt.path + this.opt.cancelOff,
          cancel = $('<' + this.opt.starType + ' />', { title: this.opt.cancelHint, 'class': this.opt.cancelClass });

      if (this.opt.starType === 'img') {
        cancel.attr({ src: icon, alt: 'x' });
      } else {
        // TODO: use $.data
        cancel.attr('data-alt', 'x').addClass(icon);
      }

      if (this.opt.cancelPlace === 'left') {
        this.self.prepend('&#160;').prepend(cancel);
      } else {
        this.self.append('&#160;').append(cancel);
      }

      this.cancel = cancel;
    },

    _createScore: function() {
      var score = $(this.opt.targetScore);

      this.score = score.length ? score : methods._buildScoreField.call(this);
    },

    _createStars: function() {
      for (var i = 1; i <= this.opt.number; i++) {
        var
          name  = methods._nameForIndex.call(this, i),
          attrs = { alt: i, src: this.opt.path + this.opt[name] };

        if (this.opt.starType !== 'img') {
          attrs = { 'data-alt': i, 'class': attrs.src }; // TODO: use $.data.
        }

        attrs.title = methods._getHint.call(this, i);

        $('<' + this.opt.starType + ' />', attrs).appendTo(this);

        if (this.opt.space) {
          this.self.append(i < this.opt.number ? '&#160;' : '');
        }
      }

      this.stars = this.self.children(this.opt.starType);
    },

    _error: function(message) {
      $(this).text(message);

      $.error(message);
    },

    _fill: function(score) {
      var hash = 0;

      for (var i = 1; i <= this.stars.length; i++) {
        var
          icon,
          star   = this.stars[i - 1],
          turnOn = methods._turnOn.call(this, i, score);

        if (this.opt.iconRange && this.opt.iconRange.length > hash) {
          var irange = this.opt.iconRange[hash];

          icon = methods._getRangeIcon.call(this, irange, turnOn);

          if (i <= irange.range) {
            methods._setIcon.call(this, star, icon);
          }

          if (i === irange.range) {
            hash++;
          }
        } else {
          icon = this.opt[turnOn ? 'starOn' : 'starOff'];

          methods._setIcon.call(this, star, icon);
        }
      }
    },

    _getFirstDecimal: function(number) {
      var
        decimal = number.toString().split('.')[1],
        result  = 0;

      if (decimal) {
        result = parseInt(decimal.charAt(0), 10);

        if (decimal.slice(1, 5) === '9999') {
          result++;
        }
      }

      return result;
    },

    _getRangeIcon: function(irange, turnOn) {
      return turnOn ? irange.on || this.opt.starOn : irange.off || this.opt.starOff;
    },

    _getScoreByPosition: function(evt, icon) {
      var score = parseInt(icon.alt || icon.getAttribute('data-alt'), 10);

      if (this.opt.half) {
        var
          size    = methods._getWidth.call(this),
          percent = parseFloat((evt.pageX - $(icon).offset().left) / size);

        score = score - 1 + percent;
      }

      return score;
    },

    _getHint: function(score, evt) {
      if (score !== 0 && !score) {
        return this.opt.noRatedMsg;
      }

      var
        decimal = methods._getFirstDecimal.call(this, score),
        integer = Math.ceil(score),
        group   = this.opt.hints[(integer || 1) - 1],
        hint    = group,
        set     = !evt || this.move;

      if (this.opt.precision) {
        if (set) {
          decimal = decimal === 0 ? 9 : decimal - 1;
        }

        hint = group[decimal];
      } else if (this.opt.halfShow || this.opt.half) {
        decimal = set && decimal === 0 ? 1 : decimal > 5 ? 1 : 0;

        hint = group[decimal];
      }

      return hint === '' ? '' : hint || score;
    },

    _getWidth: function() {
      var width = this.stars[0].width || parseFloat(this.stars.eq(0).css('font-size'));

      if (!width) {
        methods._error.call(this, 'Could not get the icon width!');
      }

      return width;
    },

    _lock: function() {
      var hint = methods._getHint.call(this, this.score.val());

      this.style.cursor = '';
      this.title        = hint;

      this.score.prop('readonly', true);
      this.stars.prop('title', hint);

      if (this.cancel) {
        this.cancel.hide();
      }

      this.self.data('readonly', true);
    },

    _nameForIndex: function(i) {
      return this.opt.score && this.opt.score >= i ? 'starOn' : 'starOff';
    },

    _resetTitle: function(star) {
      for (var i = 0; i < this.opt.number; i++) {
        this.stars[i].title = methods._getHint.call(this, i + 1);
      }
    },

     _roundHalfScore: function(score) {
      var integer = parseInt(score, 10),
          decimal = methods._getFirstDecimal.call(this, score);

      if (decimal !== 0) {
        decimal = decimal > 5 ? 1 : 0.5;
      }

      return integer + decimal;
    },

    _roundStars: function(score, evt) {
      var
        decimal = (score % 1).toFixed(2),
        name    ;

      if (evt || this.move) {
        name = decimal > 0.5 ? 'starOn' : 'starHalf';
      } else if (decimal > this.opt.round.down) {               // Up:   [x.76 .. x.99]
        name = 'starOn';

        if (this.opt.halfShow && decimal < this.opt.round.up) { // Half: [x.26 .. x.75]
          name = 'starHalf';
        } else if (decimal < this.opt.round.full) {             // Down: [x.00 .. x.5]
          name = 'starOff';
        }
      }

      if (name) {
        var
          icon = this.opt[name],
          star = this.stars[Math.ceil(score) - 1];

        methods._setIcon.call(this, star, icon);
      }                                                         // Full down: [x.00 .. x.25]
    },

    _setIcon: function(star, icon) {
      star[this.opt.starType === 'img' ? 'src' : 'className'] = this.opt.path + icon;
    },

    _setTarget: function(target, score) {
      if (score) {
        score = this.opt.targetFormat.toString().replace('{score}', score);
      }

      if (target.is(':input')) {
        target.val(score);
      } else {
        target.html(score);
      }
    },

    _setTitle: function(score, evt) {
      if (score) {
        var
          integer = parseInt(Math.ceil(score), 10),
          star    = this.stars[integer - 1];

        star.title = methods._getHint.call(this, score, evt);
      }
    },

    _target: function(score, evt) {
      if (this.opt.target) {
        var target = $(this.opt.target);

        if (!target.length) {
          methods._error.call(this, 'Target selector invalid or missing!');
        }

        var mouseover = evt && evt.type === 'mouseover';

        if (score === undefined) {
          score = this.opt.targetText;
        } else if (score === null) {
          score = mouseover ? this.opt.cancelHint : this.opt.targetText;
        } else {
          if (this.opt.targetType === 'hint') {
            score = methods._getHint.call(this, score, evt);
          } else if (this.opt.precision) {
            score = parseFloat(score).toFixed(1);
          }

          var mousemove = evt && evt.type === 'mousemove';

          if (!mouseover && !mousemove && !this.opt.targetKeep) {
            score = this.opt.targetText;
          }
        }

        methods._setTarget.call(this, target, score);
      }
    },

    _turnOn: function(i, score) {
      return this.opt.single ? (i === score) : (i <= score);
    },

    _unlock: function() {
      this.style.cursor = 'pointer';
      this.removeAttribute('title');

      this.score.removeAttr('readonly');

      this.self.data('readonly', false);

      for (var i = 0; i < this.opt.number; i++) {
        this.stars[i].title = methods._getHint.call(this, i + 1);
      }

      if (this.cancel) {
        this.cancel.css('display', '');
      }
    },

    cancel: function(click) {
      return this.each(function() {
        var self = $(this);

        if (self.data('readonly') !== true) {
          methods[click ? 'click' : 'score'].call(self, null);

          this.score.removeAttr('value');
        }
      });
    },

    click: function(score) {
      return this.each(function() {
        if ($(this).data('readonly') !== true) {
          score = methods._adjustedScore.call(this, score);

          methods._apply.call(this, score);

          if (this.opt.click) {
            this.opt.click.call(this, score, $.Event('click'));
          }

          methods._target.call(this, score);
        }
      });
    },

    destroy: function() {
      return this.each(function() {
        var self = $(this),
            raw  = self.data('raw');

        if (raw) {
          self.off('.raty').empty().css({ cursor: raw.style.cursor }).removeData('readonly');
        } else {
          self.data('raw', self.clone()[0]);
        }
      });
    },

    getScore: function() {
      var score = [],
          value ;

      this.each(function() {
        value = this.score.val();

        score.push(value ? +value : undefined);
      });

      return (score.length > 1) ? score : score[0];
    },

    move: function(score) {
      return this.each(function() {
        var
          integer  = parseInt(score, 10),
          decimal  = methods._getFirstDecimal.call(this, score);

        if (integer >= this.opt.number) {
          integer = this.opt.number - 1;
          decimal = 10;
        }

        var
          width   = methods._getWidth.call(this),
          steps   = width / 10,
          star    = $(this.stars[integer]),
          percent = star.offset().left + steps * decimal,
          evt     = $.Event('mousemove', { pageX: percent });

        this.move = true;

        star.trigger(evt);

        this.move = false;
      });
    },

    readOnly: function(readonly) {
      return this.each(function() {
        var self = $(this);

        if (self.data('readonly') !== readonly) {
          if (readonly) {
            self.off('.raty').children('img').off('.raty');

            methods._lock.call(this);
          } else {
            methods._binds.call(this);
            methods._unlock.call(this);
          }

          self.data('readonly', readonly);
        }
      });
    },

    reload: function() {
      return methods.set.call(this, {});
    },

    score: function() {
      var self = $(this);

      return arguments.length ? methods.setScore.apply(self, arguments) : methods.getScore.call(self);
    },

    set: function(options) {
      return this.each(function() {
        $(this).raty($.extend({}, this.opt, options));
      });
    },

    setScore: function(score) {
      return this.each(function() {
        if ($(this).data('readonly') !== true) {
          score = methods._adjustedScore.call(this, score);

          methods._apply.call(this, score);
          methods._target.call(this, score);
        }
      });
    }
  };

  $.fn.raty = function(method) {
    if (methods[method]) {
      return methods[method].apply(this, Array.prototype.slice.call(arguments, 1));
    } else if (typeof method === 'object' || !method) {
      return methods.init.apply(this, arguments);
    } else {
      $.error('Method ' + method + ' does not exist!');
    }
  };

  $.fn.raty.defaults = {
    cancel       : false,
    cancelClass  : 'raty-cancel',
    cancelHint   : 'Cancel this rating!',
    cancelOff    : 'cancel-off.png',
    cancelOn     : 'cancel-on.png',
    cancelPlace  : 'left',
    click        : undefined,
    half         : false,
    halfShow     : true,
    hints        : ['bad', 'poor', 'regular', 'good', 'gorgeous'],
    iconRange    : undefined,
    mouseout     : undefined,
    mouseover    : undefined,
    noRatedMsg   : 'Not rated yet!',
    number       : 5,
    numberMax    : 20,
    path         : undefined,
    precision    : false,
    readOnly     : false,
    round        : { down: 0.25, full: 0.6, up: 0.76 },
    score        : undefined,
    scoreName    : 'score',
    single       : false,
    space        : true,
    starHalf     : 'star-half.png',
    starOff      : 'star-off.png',
    starOn       : 'star-on.png',
    starType     : 'img',
    target       : undefined,
    targetFormat : '{score}',
    targetKeep   : false,
    targetScore  : undefined,
    targetText   : '',
    targetType   : 'hint'
  };

})(jQuery);

// tipsy, facebook style tooltips for jquery
// version 1.0.0a
// (c) 2008-2010 jason frame [jason@onehackoranother.com]
// releated under the MIT license

(function($) {

    function fixTitle($ele) {
        if ($ele.attr('title') || typeof($ele.attr('original-title')) != 'string') {
            $ele.attr('original-title', $ele.attr('title') || '').removeAttr('title');
        }
    }

    function Tipsy(element, options) {
        this.$element = $(element);
        this.options = options;
        this.enabled = true;
        fixTitle(this.$element);
    }

    Tipsy.prototype = {
        show: function() {
            var title = this.getTitle();
            if (title && this.enabled) {
                var $tip = this.tip();

                $tip.find('.tipsy-inner')[this.options.html ? 'html' : 'text'](title);
                $tip[0].className = 'tipsy'; // reset classname in case of dynamic gravity
                $tip.remove().css({
                    top: 0,
                    left: 0,
                    visibility: 'hidden',
                    display: 'block'
                }).appendTo(document.body);

                var pos = $.extend({}, this.$element.offset(), {
                    width: this.$element[0].offsetWidth,
                    height: this.$element[0].offsetHeight
                });

                var actualWidth = $tip[0].offsetWidth,
                    actualHeight = $tip[0].offsetHeight;
                var gravity = (typeof this.options.gravity == 'function') ? this.options.gravity.call(this.$element[0]) : this.options.gravity;

                var tp;
                switch (gravity.charAt(0)) {
                    case 'n':
                        tp = {
                            top: pos.top + pos.height + this.options.offset,
                            left: pos.left + pos.width / 2 - actualWidth / 2
                        };
                        break;
                    case 's':
                        tp = {
                            top: pos.top - actualHeight - this.options.offset,
                            left: pos.left + pos.width / 2 - actualWidth / 2
                        };
                        break;
                    case 'e':
                        tp = {
                            top: pos.top + pos.height / 2 - actualHeight / 2,
                            left: pos.left - actualWidth - this.options.offset
                        };
                        break;
                    case 'w':
                        tp = {
                            top: pos.top + pos.height / 2 - actualHeight / 2,
                            left: pos.left + pos.width + this.options.offset
                        };
                        break;
                }

                if (gravity.length == 2) {
                    if (gravity.charAt(1) == 'w') {
                        tp.left = pos.left + pos.width / 2 - 15;
                    } else {
                        tp.left = pos.left + pos.width / 2 - actualWidth + 15;
                    }
                }

                $tip.css(tp).addClass('tipsy-' + gravity);

                if (this.options.fade) {
                    $tip.stop().css({
                        opacity: 0,
                        display: 'block',
                        visibility: 'visible'
                    }).animate({
                        opacity: this.options.opacity
                    });
                } else {
                    $tip.css({
                        visibility: 'visible',
                        opacity: this.options.opacity
                    });
                }
            }
        },

        hide: function() {
            if (this.options.fade) {
                this.tip().stop().fadeOut(function() {
                    $(this).remove();
                });
            } else {
                this.tip().remove();
            }
        },

        getTitle: function() {
            var title, $e = this.$element,
                o = this.options;
            fixTitle($e);
            var title, o = this.options;
            if (typeof o.title == 'string') {
                title = $e.attr(o.title == 'title' ? 'original-title' : o.title);
            } else if (typeof o.title == 'function') {
                title = o.title.call($e[0]);
            }
            title = ('' + title).replace(/(^\s*|\s*$)/, "");
            return title || o.fallback;
        },

        tip: function() {
            if (!this.$tip) {
                this.$tip = $('<div class="tipsy"></div>').html('<div class="tipsy-arrow"></div><div class="tipsy-inner"/></div>');
            }
            return this.$tip;
        },

        validate: function() {
            if (!this.$element[0].parentNode) {
                this.hide();
                this.$element = null;
                this.options = null;
            }
        },

        enable: function() {
            this.enabled = true;
        },
        disable: function() {
            this.enabled = false;
        },
        toggleEnabled: function() {
            this.enabled = !this.enabled;
        }
    };

    $.fn.tipsy = function(options) {

        if (options === true) {
            return this.data('tipsy');
        } else if (typeof options == 'string') {
            return this.data('tipsy')[options]();
        }

        options = $.extend({}, $.fn.tipsy.defaults, options);

        function get(ele) {
            var tipsy = $.data(ele, 'tipsy');
            if (!tipsy) {
                tipsy = new Tipsy(ele, $.fn.tipsy.elementOptions(ele, options));
                $.data(ele, 'tipsy', tipsy);
            }
            return tipsy;
        }

        function enter() {
            var tipsy = get(this);
            tipsy.hoverState = 'in';
            if (options.delayIn == 0) {
                tipsy.show();
            } else {
                setTimeout(function() {
                    if (tipsy.hoverState == 'in') tipsy.show();
                }, options.delayIn);
            }
        };

        function leave() {
            var tipsy = get(this);
            tipsy.hoverState = 'out';
            if (options.delayOut == 0) {
                tipsy.hide();
            } else {
                setTimeout(function() {
                    if (tipsy.hoverState == 'out') tipsy.hide();
                }, options.delayOut);
            }
        };

        if (!options.live) this.each(function() {
            get(this);
        });

        if (options.trigger != 'manual') {
            var binder = options.live ? 'live' : 'bind',
                eventIn = options.trigger == 'hover' ? 'mouseenter' : 'focus',
                eventOut = options.trigger == 'hover' ? 'mouseleave' : 'blur';
            this[binder](eventIn, enter)[binder](eventOut, leave);
        }

        return this;

    };

    $.fn.tipsy.defaults = {
        delayIn: 0,
        delayOut: 0,
        fade: false,
        fallback: '',
        gravity: 'n',
        html: false,
        live: false,
        offset: 0,
        opacity: 0.8,
        title: 'title',
        trigger: 'hover'
    };

    // Overwrite this method to provide options on a per-element basis.
    // For example, you could store the gravity in a 'tipsy-gravity' attribute:
    // return $.extend({}, options, {gravity: $(ele).attr('tipsy-gravity') || 'n' });
    // (remember - do not modify 'options' in place!)
    $.fn.tipsy.elementOptions = function(ele, options) {
        return $.metadata ? $.extend({}, options, $(ele).metadata()) : options;
    };

    $.fn.tipsy.autoNS = function() {
        return $(this).offset().top > ($(document).scrollTop() + $(window).height() / 2) ? 's' : 'n';
    };

    $.fn.tipsy.autoWE = function() {
        return $(this).offset().left > ($(document).scrollLeft() + $(window).width() / 2) ? 'e' : 'w';
    };

})(jQuery);

/*!
 * jQuery Validation Plugin v1.14.0
 *
 * http://jqueryvalidation.org/
 *
 * Copyright (c) 2015 Jörn Zaefferer
 * Released under the MIT license
 */
(function(factory) {
    if (typeof define === "function" && define.amd) {
        define(["static/frontend/js/jquery"], factory);
    } else {
        factory(jQuery);
    }
}(function($) {

    $.extend($.fn, {
        // http://jqueryvalidation.org/validate/
        validate: function(options) {

            // if nothing is selected, return nothing; can't chain anyway
            if (!this.length) {
                if (options && options.debug && window.console) {
                    console.warn("Nothing selected, can't validate, returning nothing.");
                }
                return;
            }

            // check if a validator for this form was already created
            var validator = $.data(this[0], "validator");
            if (validator) {
                return validator;
            }

            // Add novalidate tag if HTML5.
            this.attr("novalidate", "novalidate");

            validator = new $.validator(options, this[0]);
            $.data(this[0], "validator", validator);

            if (validator.settings.onsubmit) {

                this.on("click.validate", ":submit", function(event) {
                    if (validator.settings.submitHandler) {
                        validator.submitButton = event.target;
                    }

                    // allow suppressing validation by adding a cancel class to the submit button
                    if ($(this).hasClass("cancel")) {
                        validator.cancelSubmit = true;
                    }

                    // allow suppressing validation by adding the html5 formnovalidate attribute to the submit button
                    if ($(this).attr("formnovalidate") !== undefined) {
                        validator.cancelSubmit = true;
                    }
                });

                // validate the form on submit
                this.on("submit.validate", function(event) {
                    if (validator.settings.debug) {
                        // prevent form submit to be able to see console output
                        event.preventDefault();
                    }

                    function handle() {
                        var hidden, result;
                        if (validator.settings.submitHandler) {
                            if (validator.submitButton) {
                                // insert a hidden input as a replacement for the missing submit button
                                hidden = $("<input type='hidden'/>")
                                    .attr("name", validator.submitButton.name)
                                    .val($(validator.submitButton).val())
                                    .appendTo(validator.currentForm);
                            }
                            result = validator.settings.submitHandler.call(validator, validator.currentForm, event);
                            if (validator.submitButton) {
                                // and clean up afterwards; thanks to no-block-scope, hidden can be referenced
                                hidden.remove();
                            }
                            if (result !== undefined) {
                                return result;
                            }
                            return false;
                        }
                        return true;
                    }

                    // prevent submit for invalid forms or custom submit handlers
                    if (validator.cancelSubmit) {
                        validator.cancelSubmit = false;
                        return handle();
                    }
                    if (validator.form()) {
                        if (validator.pendingRequest) {
                            validator.formSubmitted = true;
                            return false;
                        }
                        return handle();
                    } else {
                        validator.focusInvalid();
                        return false;
                    }
                });
            }

            return validator;
        },
        // http://jqueryvalidation.org/valid/
        valid: function() {
            var valid, validator, errorList;

            if ($(this[0]).is("form")) {
                valid = this.validate().form();
            } else {
                errorList = [];
                valid = true;
                validator = $(this[0].form).validate();
                this.each(function() {
                    valid = validator.element(this) && valid;
                    errorList = errorList.concat(validator.errorList);
                });
                validator.errorList = errorList;
            }
            return valid;
        },

        // http://jqueryvalidation.org/rules/
        rules: function(command, argument) {
            var element = this[0],
                settings, staticRules, existingRules, data, param, filtered;

            if (command) {
                settings = $.data(element.form, "validator").settings;
                staticRules = settings.rules;
                existingRules = $.validator.staticRules(element);
                switch (command) {
                    case "add":
                        $.extend(existingRules, $.validator.normalizeRule(argument));
                        // remove messages from rules, but allow them to be set separately
                        delete existingRules.messages;
                        staticRules[element.name] = existingRules;
                        if (argument.messages) {
                            settings.messages[element.name] = $.extend(settings.messages[element.name], argument.messages);
                        }
                        break;
                    case "remove":
                        if (!argument) {
                            delete staticRules[element.name];
                            return existingRules;
                        }
                        filtered = {};
                        $.each(argument.split(/\s/), function(index, method) {
                            filtered[method] = existingRules[method];
                            delete existingRules[method];
                            if (method === "required") {
                                $(element).removeAttr("aria-required");
                            }
                        });
                        return filtered;
                }
            }

            data = $.validator.normalizeRules(
                $.extend({},
                    $.validator.classRules(element),
                    $.validator.attributeRules(element),
                    $.validator.dataRules(element),
                    $.validator.staticRules(element)
                ), element);

            // make sure required is at front
            if (data.required) {
                param = data.required;
                delete data.required;
                data = $.extend({
                    required: param
                }, data);
                $(element).attr("aria-required", "true");
            }

            // make sure remote is at back
            if (data.remote) {
                param = data.remote;
                delete data.remote;
                data = $.extend(data, {
                    remote: param
                });
            }

            return data;
        }
    });

    // Custom selectors
    $.extend($.expr[":"], {
        // http://jqueryvalidation.org/blank-selector/
        blank: function(a) {
            return !$.trim("" + $(a).val());
        },
        // http://jqueryvalidation.org/filled-selector/
        filled: function(a) {
            return !!$.trim("" + $(a).val());
        },
        // http://jqueryvalidation.org/unchecked-selector/
        unchecked: function(a) {
            return !$(a).prop("checked");
        }
    });

    // constructor for validator
    $.validator = function(options, form) {
        this.settings = $.extend(true, {}, $.validator.defaults, options);
        this.currentForm = form;
        this.init();
    };

    // http://jqueryvalidation.org/jQuery.validator.format/
    $.validator.format = function(source, params) {
        if (arguments.length === 1) {
            return function() {
                var args = $.makeArray(arguments);
                args.unshift(source);
                return $.validator.format.apply(this, args);
            };
        }
        if (arguments.length > 2 && params.constructor !== Array) {
            params = $.makeArray(arguments).slice(1);
        }
        if (params.constructor !== Array) {
            params = [params];
        }
        $.each(params, function(i, n) {
            source = source.replace(new RegExp("\\{" + i + "\\}", "g"), function() {
                return n;
            });
        });
        return source;
    };

    $.extend($.validator, {

        defaults: {
            messages: {},
            groups: {},
            rules: {},
            errorClass: "error",
            validClass: "valid",
            errorElement: "label",
            focusCleanup: false,
            focusInvalid: true,
            errorContainer: $([]),
            errorLabelContainer: $([]),
            onsubmit: true,
            ignore: ":hidden",
            ignoreTitle: false,
            onfocusin: function(element) {
                this.lastActive = element;

                // Hide error label and remove error class on focus if enabled
                if (this.settings.focusCleanup) {
                    if (this.settings.unhighlight) {
                        this.settings.unhighlight.call(this, element, this.settings.errorClass, this.settings.validClass);
                    }
                    this.hideThese(this.errorsFor(element));
                }
            },
            onfocusout: function(element) {
                if (!this.checkable(element) && (element.name in this.submitted || !this.optional(element))) {
                    this.element(element);
                }
            },
            onkeyup: function(element, event) {
                // Avoid revalidate the field when pressing one of the following keys
                // Shift       => 16
                // Ctrl        => 17
                // Alt         => 18
                // Caps lock   => 20
                // End         => 35
                // Home        => 36
                // Left arrow  => 37
                // Up arrow    => 38
                // Right arrow => 39
                // Down arrow  => 40
                // Insert      => 45
                // Num lock    => 144
                // AltGr key   => 225
                var excludedKeys = [
                    16, 17, 18, 20, 35, 36, 37,
                    38, 39, 40, 45, 144, 225
                ];

                if (event.which === 9 && this.elementValue(element) === "" || $.inArray(event.keyCode, excludedKeys) !== -1) {
                    return;
                } else if (element.name in this.submitted || element === this.lastElement) {
                    this.element(element);
                }
            },
            onclick: function(element) {
                // click on selects, radiobuttons and checkboxes
                if (element.name in this.submitted) {
                    this.element(element);

                    // or option elements, check parent select in that case
                } else if (element.parentNode.name in this.submitted) {
                    this.element(element.parentNode);
                }
            },
            highlight: function(element, errorClass, validClass) {
                if (element.type === "radio") {
                    this.findByName(element.name).addClass(errorClass).removeClass(validClass);
                } else {
                    $(element).addClass(errorClass).removeClass(validClass);
                }
            },
            unhighlight: function(element, errorClass, validClass) {
                if (element.type === "radio") {
                    this.findByName(element.name).removeClass(errorClass).addClass(validClass);
                } else {
                    $(element).removeClass(errorClass).addClass(validClass);
                }
            }
        },

        // http://jqueryvalidation.org/jQuery.validator.setDefaults/
        setDefaults: function(settings) {
            $.extend($.validator.defaults, settings);
        },

        messages: {
            required: "This field is required.",
            remote: "Please fix this field.",
            email: "Please enter a valid email address.",
            url: "Please enter a valid URL.",
            date: "Please enter a valid date.",
            dateISO: "Please enter a valid date ( ISO ).",
            number: "Please enter a valid number.",
            digits: "Please enter only digits.",
            creditcard: "Please enter a valid credit card number.",
            equalTo: "Please enter the same value again.",
            maxlength: $.validator.format("Please enter no more than {0} characters."),
            minlength: $.validator.format("Please enter at least {0} characters."),
            rangelength: $.validator.format("Please enter a value between {0} and {1} characters long."),
            range: $.validator.format("Please enter a value between {0} and {1}."),
            max: $.validator.format("Please enter a value less than or equal to {0}."),
            min: $.validator.format("Please enter a value greater than or equal to {0}.")
        },

        autoCreateRanges: false,

        prototype: {

            init: function() {
                this.labelContainer = $(this.settings.errorLabelContainer);
                this.errorContext = this.labelContainer.length && this.labelContainer || $(this.currentForm);
                this.containers = $(this.settings.errorContainer).add(this.settings.errorLabelContainer);
                this.submitted = {};
                this.valueCache = {};
                this.pendingRequest = 0;
                this.pending = {};
                this.invalid = {};
                this.reset();

                var groups = (this.groups = {}),
                    rules;
                $.each(this.settings.groups, function(key, value) {
                    if (typeof value === "string") {
                        value = value.split(/\s/);
                    }
                    $.each(value, function(index, name) {
                        groups[name] = key;
                    });
                });
                rules = this.settings.rules;
                $.each(rules, function(key, value) {
                    rules[key] = $.validator.normalizeRule(value);
                });

                function delegate(event) {
                    var validator = $.data(this.form, "validator"),
                        eventType = "on" + event.type.replace(/^validate/, ""),
                        settings = validator.settings;
                    if (settings[eventType] && !$(this).is(settings.ignore)) {
                        settings[eventType].call(validator, this, event);
                    }
                }

                $(this.currentForm)
                    .on("focusin.validate focusout.validate keyup.validate",
                        ":text, [type='password'], [type='file'], select, textarea, [type='number'], [type='search'], " +
                        "[type='tel'], [type='url'], [type='email'], [type='datetime'], [type='date'], [type='month'], " +
                        "[type='week'], [type='time'], [type='datetime-local'], [type='range'], [type='color'], " +
                        "[type='radio'], [type='checkbox']", delegate)
                    // Support: Chrome, oldIE
                    // "select" is provided as event.target when clicking a option
                    .on("click.validate", "select, option, [type='radio'], [type='checkbox']", delegate);

                if (this.settings.invalidHandler) {
                    $(this.currentForm).on("invalid-form.validate", this.settings.invalidHandler);
                }

                // Add aria-required to any Static/Data/Class required fields before first validation
                // Screen readers require this attribute to be present before the initial submission http://www.w3.org/TR/WCAG-TECHS/ARIA2.html
                $(this.currentForm).find("[required], [data-rule-required], .required").attr("aria-required", "true");
            },

            // http://jqueryvalidation.org/Validator.form/
            form: function() {
                this.checkForm();
                $.extend(this.submitted, this.errorMap);
                this.invalid = $.extend({}, this.errorMap);
                if (!this.valid()) {
                    $(this.currentForm).triggerHandler("invalid-form", [this]);
                }
                this.showErrors();
                return this.valid();
            },

            checkForm: function() {
                this.prepareForm();
                for (var i = 0, elements = (this.currentElements = this.elements()); elements[i]; i++) {
                    this.check(elements[i]);
                }
                return this.valid();
            },

            // http://jqueryvalidation.org/Validator.element/
            element: function(element) {
                var cleanElement = this.clean(element),
                    checkElement = this.validationTargetFor(cleanElement),
                    result = true;

                this.lastElement = checkElement;

                if (checkElement === undefined) {
                    delete this.invalid[cleanElement.name];
                } else {
                    this.prepareElement(checkElement);
                    this.currentElements = $(checkElement);

                    result = this.check(checkElement) !== false;
                    if (result) {
                        delete this.invalid[checkElement.name];
                    } else {
                        this.invalid[checkElement.name] = true;
                    }
                }
                // Add aria-invalid status for screen readers
                $(element).attr("aria-invalid", !result);

                if (!this.numberOfInvalids()) {
                    // Hide error containers on last error
                    this.toHide = this.toHide.add(this.containers);
                }
                this.showErrors();
                return result;
            },

            // http://jqueryvalidation.org/Validator.showErrors/
            showErrors: function(errors) {
                if (errors) {
                    // add items to error list and map
                    $.extend(this.errorMap, errors);
                    this.errorList = [];
                    for (var name in errors) {
                        this.errorList.push({
                            message: errors[name],
                            element: this.findByName(name)[0]
                        });
                    }
                    // remove items from success list
                    this.successList = $.grep(this.successList, function(element) {
                        return !(element.name in errors);
                    });
                }
                if (this.settings.showErrors) {
                    this.settings.showErrors.call(this, this.errorMap, this.errorList);
                } else {
                    this.defaultShowErrors();
                }
            },

            // http://jqueryvalidation.org/Validator.resetForm/
            resetForm: function() {
                if ($.fn.resetForm) {
                    $(this.currentForm).resetForm();
                }
                this.submitted = {};
                this.lastElement = null;
                this.prepareForm();
                this.hideErrors();
                var i, elements = this.elements()
                    .removeData("previousValue")
                    .removeAttr("aria-invalid");

                if (this.settings.unhighlight) {
                    for (i = 0; elements[i]; i++) {
                        this.settings.unhighlight.call(this, elements[i],
                            this.settings.errorClass, "");
                    }
                } else {
                    elements.removeClass(this.settings.errorClass);
                }
            },

            numberOfInvalids: function() {
                return this.objectLength(this.invalid);
            },

            objectLength: function(obj) {
                /* jshint unused: false */
                var count = 0,
                    i;
                for (i in obj) {
                    count++;
                }
                return count;
            },

            hideErrors: function() {
                this.hideThese(this.toHide);
            },

            hideThese: function(errors) {
                errors.not(this.containers).text("");
                this.addWrapper(errors).hide();
            },

            valid: function() {
                return this.size() === 0;
            },

            size: function() {
                return this.errorList.length;
            },

            focusInvalid: function() {
                if (this.settings.focusInvalid) {
                    try {
                        $(this.findLastActive() || this.errorList.length && this.errorList[0].element || [])
                            .filter(":visible")
                            .focus()
                            // manually trigger focusin event; without it, focusin handler isn't called, findLastActive won't have anything to find
                            .trigger("focusin");
                    } catch (e) {
                        // ignore IE throwing errors when focusing hidden elements
                    }
                }
            },

            findLastActive: function() {
                var lastActive = this.lastActive;
                return lastActive && $.grep(this.errorList, function(n) {
                    return n.element.name === lastActive.name;
                }).length === 1 && lastActive;
            },

            elements: function() {
                var validator = this,
                    rulesCache = {};

                // select all valid inputs inside the form (no submit or reset buttons)
                return $(this.currentForm)
                    .find("input, select, textarea")
                    .not(":submit, :reset, :image, :disabled")
                    .not(this.settings.ignore)
                    .filter(function() {
                        if (!this.name && validator.settings.debug && window.console) {
                            console.error("%o has no name assigned", this);
                        }

                        // select only the first element for each name, and only those with rules specified
                        if (this.name in rulesCache || !validator.objectLength($(this).rules())) {
                            return false;
                        }

                        rulesCache[this.name] = true;
                        return true;
                    });
            },

            clean: function(selector) {
                return $(selector)[0];
            },

            errors: function() {
                var errorClass = this.settings.errorClass.split(" ").join(".");
                return $(this.settings.errorElement + "." + errorClass, this.errorContext);
            },

            reset: function() {
                this.successList = [];
                this.errorList = [];
                this.errorMap = {};
                this.toShow = $([]);
                this.toHide = $([]);
                this.currentElements = $([]);
            },

            prepareForm: function() {
                this.reset();
                this.toHide = this.errors().add(this.containers);
            },

            prepareElement: function(element) {
                this.reset();
                this.toHide = this.errorsFor(element);
            },

            elementValue: function(element) {
                var val,
                    $element = $(element),
                    type = element.type;

                if (type === "radio" || type === "checkbox") {
                    return this.findByName(element.name).filter(":checked").val();
                } else if (type === "number" && typeof element.validity !== "undefined") {
                    return element.validity.badInput ? false : $element.val();
                }

                val = $element.val();
                if (typeof val === "string") {
                    return val.replace(/\r/g, "");
                }
                return val;
            },

            check: function(element) {
                element = this.validationTargetFor(this.clean(element));

                var rules = $(element).rules(),
                    rulesCount = $.map(rules, function(n, i) {
                        return i;
                    }).length,
                    dependencyMismatch = false,
                    val = this.elementValue(element),
                    result, method, rule;

                for (method in rules) {
                    rule = {
                        method: method,
                        parameters: rules[method]
                    };
                    try {

                        result = $.validator.methods[method].call(this, val, element, rule.parameters);

                        // if a method indicates that the field is optional and therefore valid,
                        // don't mark it as valid when there are no other rules
                        if (result === "dependency-mismatch" && rulesCount === 1) {
                            dependencyMismatch = true;
                            continue;
                        }
                        dependencyMismatch = false;

                        if (result === "pending") {
                            this.toHide = this.toHide.not(this.errorsFor(element));
                            return;
                        }

                        if (!result) {
                            this.formatAndAdd(element, rule);
                            return false;
                        }
                    } catch (e) {
                        if (this.settings.debug && window.console) {
                            console.log("Exception occurred when checking element " + element.id + ", check the '" + rule.method + "' method.", e);
                        }
                        if (e instanceof TypeError) {
                            e.message += ".  Exception occurred when checking element " + element.id + ", check the '" + rule.method + "' method.";
                        }

                        throw e;
                    }
                }
                if (dependencyMismatch) {
                    return;
                }
                if (this.objectLength(rules)) {
                    this.successList.push(element);
                }
                return true;
            },

            // return the custom message for the given element and validation method
            // specified in the element's HTML5 data attribute
            // return the generic message if present and no method specific message is present
            customDataMessage: function(element, method) {
                return $(element).data("msg" + method.charAt(0).toUpperCase() +
                    method.substring(1).toLowerCase()) || $(element).data("msg");
            },

            // return the custom message for the given element name and validation method
            customMessage: function(name, method) {
                var m = this.settings.messages[name];
                return m && (m.constructor === String ? m : m[method]);
            },

            // return the first defined argument, allowing empty strings
            findDefined: function() {
                for (var i = 0; i < arguments.length; i++) {
                    if (arguments[i] !== undefined) {
                        return arguments[i];
                    }
                }
                return undefined;
            },

            defaultMessage: function(element, method) {
                return this.findDefined(
                    this.customMessage(element.name, method),
                    this.customDataMessage(element, method),
                    // title is never undefined, so handle empty string as undefined
                    !this.settings.ignoreTitle && element.title || undefined,
                    $.validator.messages[method],
                    "<strong>Warning: No message defined for " + element.name + "</strong>"
                );
            },

            formatAndAdd: function(element, rule) {
                var message = this.defaultMessage(element, rule.method),
                    theregex = /\$?\{(\d+)\}/g;
                if (typeof message === "function") {
                    message = message.call(this, rule.parameters, element);
                } else if (theregex.test(message)) {
                    message = $.validator.format(message.replace(theregex, "{$1}"), rule.parameters);
                }
                this.errorList.push({
                    message: message,
                    element: element,
                    method: rule.method
                });

                this.errorMap[element.name] = message;
                this.submitted[element.name] = message;
            },

            addWrapper: function(toToggle) {
                if (this.settings.wrapper) {
                    toToggle = toToggle.add(toToggle.parent(this.settings.wrapper));
                }
                return toToggle;
            },

            defaultShowErrors: function() {
                var i, elements, error;
                for (i = 0; this.errorList[i]; i++) {
                    error = this.errorList[i];
                    if (this.settings.highlight) {
                        this.settings.highlight.call(this, error.element, this.settings.errorClass, this.settings.validClass);
                    }
                    this.showLabel(error.element, error.message);
                }
                if (this.errorList.length) {
                    this.toShow = this.toShow.add(this.containers);
                }
                if (this.settings.success) {
                    for (i = 0; this.successList[i]; i++) {
                        this.showLabel(this.successList[i]);
                    }
                }
                if (this.settings.unhighlight) {
                    for (i = 0, elements = this.validElements(); elements[i]; i++) {
                        this.settings.unhighlight.call(this, elements[i], this.settings.errorClass, this.settings.validClass);
                    }
                }
                this.toHide = this.toHide.not(this.toShow);
                this.hideErrors();
                this.addWrapper(this.toShow).show();
            },

            validElements: function() {
                return this.currentElements.not(this.invalidElements());
            },

            invalidElements: function() {
                return $(this.errorList).map(function() {
                    return this.element;
                });
            },

            showLabel: function(element, message) {
                var place, group, errorID,
                    error = this.errorsFor(element),
                    elementID = this.idOrName(element),
                    describedBy = $(element).attr("aria-describedby");
                if (error.length) {
                    // refresh error/success class
                    error.removeClass(this.settings.validClass).addClass(this.settings.errorClass);
                    // replace message on existing label
                    error.html(message);
                } else {
                    // create error element
                    error = $("<" + this.settings.errorElement + ">")
                        .attr("id", elementID + "-error")
                        .addClass(this.settings.errorClass)
                        .html(message || "");

                    // Maintain reference to the element to be placed into the DOM
                    place = error;
                    if (this.settings.wrapper) {
                        // make sure the element is visible, even in IE
                        // actually showing the wrapped element is handled elsewhere
                        place = error.hide().show().wrap("<" + this.settings.wrapper + "/>").parent();
                    }
                    if (this.labelContainer.length) {
                        this.labelContainer.append(place);
                    } else if (this.settings.errorPlacement) {
                        this.settings.errorPlacement(place, $(element));
                    } else {
                        place.insertAfter(element);
                    }

                    // Link error back to the element
                    if (error.is("label")) {
                        // If the error is a label, then associate using 'for'
                        error.attr("for", elementID);
                    } else if (error.parents("label[for='" + elementID + "']").length === 0) {
                        // If the element is not a child of an associated label, then it's necessary
                        // to explicitly apply aria-describedby

                        errorID = error.attr("id").replace(/(:|\.|\[|\]|\$)/g, "\\$1");
                        // Respect existing non-error aria-describedby
                        if (!describedBy) {
                            describedBy = errorID;
                        } else if (!describedBy.match(new RegExp("\\b" + errorID + "\\b"))) {
                            // Add to end of list if not already present
                            describedBy += " " + errorID;
                        }
                        $(element).attr("aria-describedby", describedBy);

                        // If this element is grouped, then assign to all elements in the same group
                        group = this.groups[element.name];
                        if (group) {
                            $.each(this.groups, function(name, testgroup) {
                                if (testgroup === group) {
                                    $("[name='" + name + "']", this.currentForm)
                                        .attr("aria-describedby", error.attr("id"));
                                }
                            });
                        }
                    }
                }
                if (!message && this.settings.success) {
                    error.text("");
                    if (typeof this.settings.success === "string") {
                        error.addClass(this.settings.success);
                    } else {
                        this.settings.success(error, element);
                    }
                }
                this.toShow = this.toShow.add(error);
            },

            errorsFor: function(element) {
                var name = this.idOrName(element),
                    describer = $(element).attr("aria-describedby"),
                    selector = "label[for='" + name + "'], label[for='" + name + "'] *";

                // aria-describedby should directly reference the error element
                if (describer) {
                    selector = selector + ", #" + describer.replace(/\s+/g, ", #");
                }
                return this
                    .errors()
                    .filter(selector);
            },

            idOrName: function(element) {
                return this.groups[element.name] || (this.checkable(element) ? element.name : element.id || element.name);
            },

            validationTargetFor: function(element) {

                // If radio/checkbox, validate first element in group instead
                if (this.checkable(element)) {
                    element = this.findByName(element.name);
                }

                // Always apply ignore filter
                return $(element).not(this.settings.ignore)[0];
            },

            checkable: function(element) {
                return (/radio|checkbox/i).test(element.type);
            },

            findByName: function(name) {
                return $(this.currentForm).find("[name='" + name + "']");
            },

            getLength: function(value, element) {
                switch (element.nodeName.toLowerCase()) {
                    case "select":
                        return $("option:selected", element).length;
                    case "input":
                        if (this.checkable(element)) {
                            return this.findByName(element.name).filter(":checked").length;
                        }
                }
                return value.length;
            },

            depend: function(param, element) {
                return this.dependTypes[typeof param] ? this.dependTypes[typeof param](param, element) : true;
            },

            dependTypes: {
                "boolean": function(param) {
                    return param;
                },
                "string": function(param, element) {
                    return !!$(param, element.form).length;
                },
                "function": function(param, element) {
                    return param(element);
                }
            },

            optional: function(element) {
                var val = this.elementValue(element);
                return !$.validator.methods.required.call(this, val, element) && "dependency-mismatch";
            },

            startRequest: function(element) {
                if (!this.pending[element.name]) {
                    this.pendingRequest++;
                    this.pending[element.name] = true;
                }
            },

            stopRequest: function(element, valid) {
                this.pendingRequest--;
                // sometimes synchronization fails, make sure pendingRequest is never < 0
                if (this.pendingRequest < 0) {
                    this.pendingRequest = 0;
                }
                delete this.pending[element.name];
                if (valid && this.pendingRequest === 0 && this.formSubmitted && this.form()) {
                    $(this.currentForm).submit();
                    this.formSubmitted = false;
                } else if (!valid && this.pendingRequest === 0 && this.formSubmitted) {
                    $(this.currentForm).triggerHandler("invalid-form", [this]);
                    this.formSubmitted = false;
                }
            },

            previousValue: function(element) {
                return $.data(element, "previousValue") || $.data(element, "previousValue", {
                    old: null,
                    valid: true,
                    message: this.defaultMessage(element, "remote")
                });
            },

            // cleans up all forms and elements, removes validator-specific events
            destroy: function() {
                this.resetForm();

                $(this.currentForm)
                    .off(".validate")
                    .removeData("validator");
            }

        },

        classRuleSettings: {
            required: {
                required: true
            },
            email: {
                email: true
            },
            url: {
                url: true
            },
            date: {
                date: true
            },
            dateISO: {
                dateISO: true
            },
            number: {
                number: true
            },
            digits: {
                digits: true
            },
            creditcard: {
                creditcard: true
            }
        },

        addClassRules: function(className, rules) {
            if (className.constructor === String) {
                this.classRuleSettings[className] = rules;
            } else {
                $.extend(this.classRuleSettings, className);
            }
        },

        classRules: function(element) {
            var rules = {},
                classes = $(element).attr("class");

            if (classes) {
                $.each(classes.split(" "), function() {
                    if (this in $.validator.classRuleSettings) {
                        $.extend(rules, $.validator.classRuleSettings[this]);
                    }
                });
            }
            return rules;
        },

        normalizeAttributeRule: function(rules, type, method, value) {

            // convert the value to a number for number inputs, and for text for backwards compability
            // allows type="date" and others to be compared as strings
            if (/min|max/.test(method) && (type === null || /number|range|text/.test(type))) {
                value = Number(value);

                // Support Opera Mini, which returns NaN for undefined minlength
                if (isNaN(value)) {
                    value = undefined;
                }
            }

            if (value || value === 0) {
                rules[method] = value;
            } else if (type === method && type !== "range") {

                // exception: the jquery validate 'range' method
                // does not test for the html5 'range' type
                rules[method] = true;
            }
        },

        attributeRules: function(element) {
            var rules = {},
                $element = $(element),
                type = element.getAttribute("type"),
                method, value;

            for (method in $.validator.methods) {

                // support for <input required> in both html5 and older browsers
                if (method === "required") {
                    value = element.getAttribute(method);

                    // Some browsers return an empty string for the required attribute
                    // and non-HTML5 browsers might have required="" markup
                    if (value === "") {
                        value = true;
                    }

                    // force non-HTML5 browsers to return bool
                    value = !!value;
                } else {
                    value = $element.attr(method);
                }

                this.normalizeAttributeRule(rules, type, method, value);
            }

            // maxlength may be returned as -1, 2147483647 ( IE ) and 524288 ( safari ) for text inputs
            if (rules.maxlength && /-1|2147483647|524288/.test(rules.maxlength)) {
                delete rules.maxlength;
            }

            return rules;
        },

        dataRules: function(element) {
            var rules = {},
                $element = $(element),
                type = element.getAttribute("type"),
                method, value;

            for (method in $.validator.methods) {
                value = $element.data("rule" + method.charAt(0).toUpperCase() + method.substring(1).toLowerCase());
                this.normalizeAttributeRule(rules, type, method, value);
            }
            return rules;
        },

        staticRules: function(element) {
            var rules = {},
                validator = $.data(element.form, "validator");

            if (validator.settings.rules) {
                rules = $.validator.normalizeRule(validator.settings.rules[element.name]) || {};
            }
            return rules;
        },

        normalizeRules: function(rules, element) {
            // handle dependency check
            $.each(rules, function(prop, val) {
                // ignore rule when param is explicitly false, eg. required:false
                if (val === false) {
                    delete rules[prop];
                    return;
                }
                if (val.param || val.depends) {
                    var keepRule = true;
                    switch (typeof val.depends) {
                        case "string":
                            keepRule = !!$(val.depends, element.form).length;
                            break;
                        case "function":
                            keepRule = val.depends.call(element, element);
                            break;
                    }
                    if (keepRule) {
                        rules[prop] = val.param !== undefined ? val.param : true;
                    } else {
                        delete rules[prop];
                    }
                }
            });

            // evaluate parameters
            $.each(rules, function(rule, parameter) {
                rules[rule] = $.isFunction(parameter) ? parameter(element) : parameter;
            });

            // clean number parameters
            $.each(["minlength", "maxlength"], function() {
                if (rules[this]) {
                    rules[this] = Number(rules[this]);
                }
            });
            $.each(["rangelength", "range"], function() {
                var parts;
                if (rules[this]) {
                    if ($.isArray(rules[this])) {
                        rules[this] = [Number(rules[this][0]), Number(rules[this][1])];
                    } else if (typeof rules[this] === "string") {
                        parts = rules[this].replace(/[\[\]]/g, "").split(/[\s,]+/);
                        rules[this] = [Number(parts[0]), Number(parts[1])];
                    }
                }
            });

            if ($.validator.autoCreateRanges) {
                // auto-create ranges
                if (rules.min != null && rules.max != null) {
                    rules.range = [rules.min, rules.max];
                    delete rules.min;
                    delete rules.max;
                }
                if (rules.minlength != null && rules.maxlength != null) {
                    rules.rangelength = [rules.minlength, rules.maxlength];
                    delete rules.minlength;
                    delete rules.maxlength;
                }
            }

            return rules;
        },

        // Converts a simple string to a {string: true} rule, e.g., "required" to {required:true}
        normalizeRule: function(data) {
            if (typeof data === "string") {
                var transformed = {};
                $.each(data.split(/\s/), function() {
                    transformed[this] = true;
                });
                data = transformed;
            }
            return data;
        },

        // http://jqueryvalidation.org/jQuery.validator.addMethod/
        addMethod: function(name, method, message) {
            $.validator.methods[name] = method;
            $.validator.messages[name] = message !== undefined ? message : $.validator.messages[name];
            if (method.length < 3) {
                $.validator.addClassRules(name, $.validator.normalizeRule(name));
            }
        },

        methods: {

            // http://jqueryvalidation.org/required-method/
            required: function(value, element, param) {
                // check if dependency is met
                if (!this.depend(param, element)) {
                    return "dependency-mismatch";
                }
                if (element.nodeName.toLowerCase() === "select") {
                    // could be an array for select-multiple or a string, both are fine this way
                    var val = $(element).val();
                    return val && val.length > 0;
                }
                if (this.checkable(element)) {
                    return this.getLength(value, element) > 0;
                }
                return value.length > 0;
            },

            // http://jqueryvalidation.org/email-method/
            email: function(value, element) {
                // From http://svn.php.net/viewvc/php/php-src/trunk/ext/filter/logical_filters.c?view=markup
                // Retrieved 2014-01-14
                // If you have a problem with this implementation, report a bug against the above spec
                // Or use custom methods to implement your own email validation
                return this.optional(element) || /^(?!(?:(?:\x22?\x5C[\x00-\x7E]\x22?)|(?:\x22?[^\x5C\x22]\x22?)){255,})(?!(?:(?:\x22?\x5C[\x00-\x7E]\x22?)|(?:\x22?[^\x5C\x22]\x22?)){65,}@)(?:(?:[\x21\x23-\x27\x2A\x2B\x2D\x2F-\x39\x3D\x3F\x5E-\x7E]+)|(?:\x22(?:[\x01-\x08\x0B\x0C\x0E-\x1F\x21\x23-\x5B\x5D-\x7F]|(?:\x5C[\x00-\x7F]))*\x22))(?:\.(?:(?:[\x21\x23-\x27\x2A\x2B\x2D\x2F-\x39\x3D\x3F\x5E-\x7E]+)|(?:\x22(?:[\x01-\x08\x0B\x0C\x0E-\x1F\x21\x23-\x5B\x5D-\x7F]|(?:\x5C[\x00-\x7F]))*\x22)))*@(?:(?:(?!.*[^.]{64,})(?:(?:(?:xn--)?[a-z0-9]+(?:-[a-z0-9]+)*\.){1,126}){1,}(?:(?:[a-z][a-z0-9]*)|(?:(?:xn--)[a-z0-9]+))(?:-[a-z0-9]+)*)|(?:\[(?:(?:IPv6:(?:(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){7})|(?:(?!(?:.*[a-f0-9][:\]]){7,})(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,5})?::(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,5})?)))|(?:(?:IPv6:(?:(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){5}:)|(?:(?!(?:.*[a-f0-9]:){5,})(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,3})?::(?:[a-f0-9]{1,4}(?::[a-f0-9]{1,4}){0,3}:)?)))?(?:(?:25[0-5])|(?:2[0-4][0-9])|(?:1[0-9]{2})|(?:[1-9]?[0-9]))(?:\.(?:(?:25[0-5])|(?:2[0-4][0-9])|(?:1[0-9]{2})|(?:[1-9]?[0-9]))){3}))\]))$/i.test(value);
            },

            // http://jqueryvalidation.org/url-method/
            url: function(value, element) {

                // Copyright (c) 2010-2013 Diego Perini, MIT licensed
                // https://gist.github.com/dperini/729294
                // see also https://mathiasbynens.be/demo/url-regex
                // modified to allow protocol-relative URLs
                return this.optional(element) || /^(?:(?:(?:https?|ftp):)?\/\/)(?:\S+(?::\S*)?@)?(?:(?!(?:10|127)(?:\.\d{1,3}){3})(?!(?:169\.254|192\.168)(?:\.\d{1,3}){2})(?!172\.(?:1[6-9]|2\d|3[0-1])(?:\.\d{1,3}){2})(?:[1-9]\d?|1\d\d|2[01]\d|22[0-3])(?:\.(?:1?\d{1,2}|2[0-4]\d|25[0-5])){2}(?:\.(?:[1-9]\d?|1\d\d|2[0-4]\d|25[0-4]))|(?:(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)(?:\.(?:[a-z\u00a1-\uffff0-9]-*)*[a-z\u00a1-\uffff0-9]+)*(?:\.(?:[a-z\u00a1-\uffff]{2,})).?)(?::\d{2,5})?(?:[/?#]\S*)?$/i.test(value);
            },

            // http://jqueryvalidation.org/date-method/
            date: function(value, element) {
                return this.optional(element) || !/Invalid|NaN/.test(new Date(value).toString());
            },

            // http://jqueryvalidation.org/dateISO-method/
            dateISO: function(value, element) {
                return this.optional(element) || /^\d{4}[\/\-](0?[1-9]|1[012])[\/\-](0?[1-9]|[12][0-9]|3[01])$/.test(value);
            },

            // http://jqueryvalidation.org/number-method/
            number: function(value, element) {
                return this.optional(element) || /^(?:-?\d+|-?\d{1,3}(?:,\d{3})+)?(?:\.\d+)?$/.test(value);
            },

            // http://jqueryvalidation.org/digits-method/
            digits: function(value, element) {
                return this.optional(element) || /^\d+$/.test(value);
            },

            // http://jqueryvalidation.org/creditcard-method/
            // based on http://en.wikipedia.org/wiki/Luhn_algorithm
            creditcard: function(value, element) {
                if (this.optional(element)) {
                    return "dependency-mismatch";
                }
                // accept only spaces, digits and dashes
                if (/[^0-9 \-]+/.test(value)) {
                    return false;
                }
                var nCheck = 0,
                    nDigit = 0,
                    bEven = false,
                    n, cDigit;

                value = value.replace(/\D/g, "");

                // Basing min and max length on
                // http://developer.ean.com/general_info/Valid_Credit_Card_Types
                if (value.length < 13 || value.length > 19) {
                    return false;
                }

                for (n = value.length - 1; n >= 0; n--) {
                    cDigit = value.charAt(n);
                    nDigit = parseInt(cDigit, 10);
                    if (bEven) {
                        if ((nDigit *= 2) > 9) {
                            nDigit -= 9;
                        }
                    }
                    nCheck += nDigit;
                    bEven = !bEven;
                }

                return (nCheck % 10) === 0;
            },

            // http://jqueryvalidation.org/minlength-method/
            minlength: function(value, element, param) {
                var length = $.isArray(value) ? value.length : this.getLength(value, element);
                return this.optional(element) || length >= param;
            },

            // http://jqueryvalidation.org/maxlength-method/
            maxlength: function(value, element, param) {
                var length = $.isArray(value) ? value.length : this.getLength(value, element);
                return this.optional(element) || length <= param;
            },

            // http://jqueryvalidation.org/rangelength-method/
            rangelength: function(value, element, param) {
                var length = $.isArray(value) ? value.length : this.getLength(value, element);
                return this.optional(element) || (length >= param[0] && length <= param[1]);
            },

            // http://jqueryvalidation.org/min-method/
            min: function(value, element, param) {
                return this.optional(element) || value >= param;
            },

            // http://jqueryvalidation.org/max-method/
            max: function(value, element, param) {
                return this.optional(element) || value <= param;
            },

            // http://jqueryvalidation.org/range-method/
            range: function(value, element, param) {
                return this.optional(element) || (value >= param[0] && value <= param[1]);
            },

            // http://jqueryvalidation.org/equalTo-method/
            equalTo: function(value, element, param) {
                // bind to the blur event of the target in order to revalidate whenever the target field is updated
                // TODO find a way to bind the event just once, avoiding the unbind-rebind overhead
                var target = $(param);
                if (this.settings.onfocusout) {
                    target.off(".validate-equalTo").on("blur.validate-equalTo", function() {
                        $(element).valid();
                    });
                }
                return value === target.val();
            },

            // http://jqueryvalidation.org/remote-method/
            remote: function(value, element, param) {
                if (this.optional(element)) {
                    return "dependency-mismatch";
                }

                var previous = this.previousValue(element),
                    validator, data;

                if (!this.settings.messages[element.name]) {
                    this.settings.messages[element.name] = {};
                }
                previous.originalMessage = this.settings.messages[element.name].remote;
                this.settings.messages[element.name].remote = previous.message;

                param = typeof param === "string" && {
                    url: param
                } || param;

                if (previous.old === value) {
                    return previous.valid;
                }

                previous.old = value;
                validator = this;
                this.startRequest(element);
                data = {};
                data[element.name] = value;
                $.ajax($.extend(true, {
                    mode: "abort",
                    port: "validate" + element.name,
                    dataType: "json",
                    data: data,
                    context: validator.currentForm,
                    success: function(response) {
                        var valid = response === true || response === "true",
                            errors, message, submitted;

                        validator.settings.messages[element.name].remote = previous.originalMessage;
                        if (valid) {
                            submitted = validator.formSubmitted;
                            validator.prepareElement(element);
                            validator.formSubmitted = submitted;
                            validator.successList.push(element);
                            delete validator.invalid[element.name];
                            validator.showErrors();
                        } else {
                            errors = {};
                            message = response || validator.defaultMessage(element, "remote");
                            errors[element.name] = previous.message = $.isFunction(message) ? message(value) : message;
                            validator.invalid[element.name] = true;
                            validator.showErrors(errors);
                        }
                        previous.valid = valid;
                        validator.stopRequest(element, valid);
                    }
                }, param));
                return "pending";
            }
        }

    });

    // ajax mode: abort
    // usage: $.ajax({ mode: "abort"[, port: "uniqueport"]});
    // if mode:"abort" is used, the previous request on that port (port can be undefined) is aborted via XMLHttpRequest.abort()

    var pendingRequests = {},
        ajax;
    // Use a prefilter if available (1.5+)
    if ($.ajaxPrefilter) {
        $.ajaxPrefilter(function(settings, _, xhr) {
            var port = settings.port;
            if (settings.mode === "abort") {
                if (pendingRequests[port]) {
                    pendingRequests[port].abort();
                }
                pendingRequests[port] = xhr;
            }
        });
    } else {
        // Proxy ajax
        ajax = $.ajax;
        $.ajax = function(settings) {
            var mode = ("mode" in settings ? settings : $.ajaxSettings).mode,
                port = ("port" in settings ? settings : $.ajaxSettings).port;
            if (mode === "abort") {
                if (pendingRequests[port]) {
                    pendingRequests[port].abort();
                }
                pendingRequests[port] = ajax.apply(this, arguments);
                return pendingRequests[port];
            }
            return ajax.apply(this, arguments);
        };
    }

}));

/*!
 * jQuery Validation Plugin v1.14.0
 *
 * http://jqueryvalidation.org/
 *
 * Copyright (c) 2015 Jörn Zaefferer
 * Released under the MIT license
 */
(function(factory) {
    if (typeof define === "function" && define.amd) {
        define(["static/frontend/js/jquery", "./jquery.validate"], factory);
    } else {
        factory(jQuery);
    }
}(function($) {

    (function() {

        function stripHtml(value) {
            // remove html tags and space chars
            return value.replace(/<.[^<>]*?>/g, " ").replace(/&nbsp;|&#160;/gi, " ")
                // remove punctuation
                .replace(/[.(),;:!?%#$'\"_+=\/\-“”’]*/g, "");
        }

        $.validator.addMethod("maxWords", function(value, element, params) {
            return this.optional(element) || stripHtml(value).match(/\b\w+\b/g).length <= params;
        }, $.validator.format("Please enter {0} words or less."));

        $.validator.addMethod("minWords", function(value, element, params) {
            return this.optional(element) || stripHtml(value).match(/\b\w+\b/g).length >= params;
        }, $.validator.format("Please enter at least {0} words."));

        $.validator.addMethod("rangeWords", function(value, element, params) {
            var valueStripped = stripHtml(value),
                regex = /\b\w+\b/g;
            return this.optional(element) || valueStripped.match(regex).length >= params[0] && valueStripped.match(regex).length <= params[1];
        }, $.validator.format("Please enter between {0} and {1} words."));

    }());

    // Accept a value from a file input based on a required mimetype
    $.validator.addMethod("accept", function(value, element, param) {
        // Split mime on commas in case we have multiple types we can accept
        var typeParam = typeof param === "string" ? param.replace(/\s/g, "").replace(/,/g, "|") : "image/*",
            optionalValue = this.optional(element),
            i, file;

        // Element is optional
        if (optionalValue) {
            return optionalValue;
        }

        if ($(element).attr("type") === "file") {
            // If we are using a wildcard, make it regex friendly
            typeParam = typeParam.replace(/\*/g, ".*");

            // Check if the element has a FileList before checking each file
            if (element.files && element.files.length) {
                for (i = 0; i < element.files.length; i++) {
                    file = element.files[i];

                    // Grab the mimetype from the loaded file, verify it matches
                    if (!file.type.match(new RegExp("\\.?(" + typeParam + ")$", "i"))) {
                        return false;
                    }
                }
            }
        }

        // Either return true because we've validated each file, or because the
        // browser does not support element.files and the FileList feature
        return true;
    }, $.validator.format("Please enter a value with a valid mimetype."));

    $.validator.addMethod("alphanumeric", function(value, element) {
        return this.optional(element) || /^\w+$/i.test(value);
    }, "Letters, numbers, and underscores only please");

    /*
     * Dutch bank account numbers (not 'giro' numbers) have 9 digits
     * and pass the '11 check'.
     * We accept the notation with spaces, as that is common.
     * acceptable: 123456789 or 12 34 56 789
     */
    $.validator.addMethod("bankaccountNL", function(value, element) {
        if (this.optional(element)) {
            return true;
        }
        if (!(/^[0-9]{9}|([0-9]{2} ){3}[0-9]{3}$/.test(value))) {
            return false;
        }
        // now '11 check'
        var account = value.replace(/ /g, ""), // remove spaces
            sum = 0,
            len = account.length,
            pos, factor, digit;
        for (pos = 0; pos < len; pos++) {
            factor = len - pos;
            digit = account.substring(pos, pos + 1);
            sum = sum + factor * digit;
        }
        return sum % 11 === 0;
    }, "Please specify a valid bank account number");

    $.validator.addMethod("bankorgiroaccountNL", function(value, element) {
        return this.optional(element) ||
            ($.validator.methods.bankaccountNL.call(this, value, element)) ||
            ($.validator.methods.giroaccountNL.call(this, value, element));
    }, "Please specify a valid bank or giro account number");

    /**
     * BIC is the business identifier code (ISO 9362). This BIC check is not a guarantee for authenticity.
     *
     * BIC pattern: BBBBCCLLbbb (8 or 11 characters long; bbb is optional)
     *
     * BIC definition in detail:
     * - First 4 characters - bank code (only letters)
     * - Next 2 characters - ISO 3166-1 alpha-2 country code (only letters)
     * - Next 2 characters - location code (letters and digits)
     *   a. shall not start with '0' or '1'
     *   b. second character must be a letter ('O' is not allowed) or one of the following digits ('0' for test (therefore not allowed), '1' for passive participant and '2' for active participant)
     * - Last 3 characters - branch code, optional (shall not start with 'X' except in case of 'XXX' for primary office) (letters and digits)
     */
    $.validator.addMethod("bic", function(value, element) {
        return this.optional(element) || /^([A-Z]{6}[A-Z2-9][A-NP-Z1-2])(X{3}|[A-WY-Z0-9][A-Z0-9]{2})?$/.test(value);
    }, "Please specify a valid BIC code");

    /*
     * Código de identificación fiscal ( CIF ) is the tax identification code for Spanish legal entities
     * Further rules can be found in Spanish on http://es.wikipedia.org/wiki/C%C3%B3digo_de_identificaci%C3%B3n_fiscal
     */
    $.validator.addMethod("cifES", function(value) {
        "use strict";

        var num = [],
            controlDigit, sum, i, count, tmp, secondDigit;

        value = value.toUpperCase();

        // Quick format test
        if (!value.match("((^[A-Z]{1}[0-9]{7}[A-Z0-9]{1}$|^[T]{1}[A-Z0-9]{8}$)|^[0-9]{8}[A-Z]{1}$)")) {
            return false;
        }

        for (i = 0; i < 9; i++) {
            num[i] = parseInt(value.charAt(i), 10);
        }

        // Algorithm for checking CIF codes
        sum = num[2] + num[4] + num[6];
        for (count = 1; count < 8; count += 2) {
            tmp = (2 * num[count]).toString();
            secondDigit = tmp.charAt(1);

            sum += parseInt(tmp.charAt(0), 10) + (secondDigit === "" ? 0 : parseInt(secondDigit, 10));
        }

        /* The first (position 1) is a letter following the following criteria:
         *	A. Corporations
         *	B. LLCs
         *	C. General partnerships
         *	D. Companies limited partnerships
         *	E. Communities of goods
         *	F. Cooperative Societies
         *	G. Associations
         *	H. Communities of homeowners in horizontal property regime
         *	J. Civil Societies
         *	K. Old format
         *	L. Old format
         *	M. Old format
         *	N. Nonresident entities
         *	P. Local authorities
         *	Q. Autonomous bodies, state or not, and the like, and congregations and religious institutions
         *	R. Congregations and religious institutions (since 2008 ORDER EHA/451/2008)
         *	S. Organs of State Administration and regions
         *	V. Agrarian Transformation
         *	W. Permanent establishments of non-resident in Spain
         */
        if (/^[ABCDEFGHJNPQRSUVW]{1}/.test(value)) {
            sum += "";
            controlDigit = 10 - parseInt(sum.charAt(sum.length - 1), 10);
            value += controlDigit;
            return (num[8].toString() === String.fromCharCode(64 + controlDigit) || num[8].toString() === value.charAt(value.length - 1));
        }

        return false;

    }, "Please specify a valid CIF number.");

    /*
     * Brazillian CPF number (Cadastrado de Pessoas Físicas) is the equivalent of a Brazilian tax registration number.
     * CPF numbers have 11 digits in total: 9 numbers followed by 2 check numbers that are being used for validation.
     */
    $.validator.addMethod("cpfBR", function(value) {
        // Removing special characters from value
        value = value.replace(/([~!@#$%^&*()_+=`{}\[\]\-|\\:;'<>,.\/? ])+/g, "");

        // Checking value to have 11 digits only
        if (value.length !== 11) {
            return false;
        }

        var sum = 0,
            firstCN, secondCN, checkResult, i;

        firstCN = parseInt(value.substring(9, 10), 10);
        secondCN = parseInt(value.substring(10, 11), 10);

        checkResult = function(sum, cn) {
            var result = (sum * 10) % 11;
            if ((result === 10) || (result === 11)) {
                result = 0;
            }
            return (result === cn);
        };

        // Checking for dump data
        if (value === "" ||
            value === "00000000000" ||
            value === "11111111111" ||
            value === "22222222222" ||
            value === "33333333333" ||
            value === "44444444444" ||
            value === "55555555555" ||
            value === "66666666666" ||
            value === "77777777777" ||
            value === "88888888888" ||
            value === "99999999999"
        ) {
            return false;
        }

        // Step 1 - using first Check Number:
        for (i = 1; i <= 9; i++) {
            sum = sum + parseInt(value.substring(i - 1, i), 10) * (11 - i);
        }

        // If first Check Number (CN) is valid, move to Step 2 - using second Check Number:
        if (checkResult(sum, firstCN)) {
            sum = 0;
            for (i = 1; i <= 10; i++) {
                sum = sum + parseInt(value.substring(i - 1, i), 10) * (12 - i);
            }
            return checkResult(sum, secondCN);
        }
        return false;

    }, "Please specify a valid CPF number");

    /* NOTICE: Modified version of Castle.Components.Validator.CreditCardValidator
     * Redistributed under the the Apache License 2.0 at http://www.apache.org/licenses/LICENSE-2.0
     * Valid Types: mastercard, visa, amex, dinersclub, enroute, discover, jcb, unknown, all (overrides all other settings)
     */
    $.validator.addMethod("creditcardtypes", function(value, element, param) {
        if (/[^0-9\-]+/.test(value)) {
            return false;
        }

        value = value.replace(/\D/g, "");

        var validTypes = 0x0000;

        if (param.mastercard) {
            validTypes |= 0x0001;
        }
        if (param.visa) {
            validTypes |= 0x0002;
        }
        if (param.amex) {
            validTypes |= 0x0004;
        }
        if (param.dinersclub) {
            validTypes |= 0x0008;
        }
        if (param.enroute) {
            validTypes |= 0x0010;
        }
        if (param.discover) {
            validTypes |= 0x0020;
        }
        if (param.jcb) {
            validTypes |= 0x0040;
        }
        if (param.unknown) {
            validTypes |= 0x0080;
        }
        if (param.all) {
            validTypes = 0x0001 | 0x0002 | 0x0004 | 0x0008 | 0x0010 | 0x0020 | 0x0040 | 0x0080;
        }
        if (validTypes & 0x0001 && /^(5[12345])/.test(value)) { //mastercard
            return value.length === 16;
        }
        if (validTypes & 0x0002 && /^(4)/.test(value)) { //visa
            return value.length === 16;
        }
        if (validTypes & 0x0004 && /^(3[47])/.test(value)) { //amex
            return value.length === 15;
        }
        if (validTypes & 0x0008 && /^(3(0[012345]|[68]))/.test(value)) { //dinersclub
            return value.length === 14;
        }
        if (validTypes & 0x0010 && /^(2(014|149))/.test(value)) { //enroute
            return value.length === 15;
        }
        if (validTypes & 0x0020 && /^(6011)/.test(value)) { //discover
            return value.length === 16;
        }
        if (validTypes & 0x0040 && /^(3)/.test(value)) { //jcb
            return value.length === 16;
        }
        if (validTypes & 0x0040 && /^(2131|1800)/.test(value)) { //jcb
            return value.length === 15;
        }
        if (validTypes & 0x0080) { //unknown
            return true;
        }
        return false;
    }, "Please enter a valid credit card number.");

    /**
     * Validates currencies with any given symbols by @jameslouiz
     * Symbols can be optional or required. Symbols required by default
     *
     * Usage examples:
     *  currency: ["£", false] - Use false for soft currency validation
     *  currency: ["$", false]
     *  currency: ["RM", false] - also works with text based symbols such as "RM" - Malaysia Ringgit etc
     *
     *  <input class="currencyInput" name="currencyInput">
     *
     * Soft symbol checking
     *  currencyInput: {
     *     currency: ["$", false]
     *  }
     *
     * Strict symbol checking (default)
     *  currencyInput: {
     *     currency: "$"
     *     //OR
     *     currency: ["$", true]
     *  }
     *
     * Multiple Symbols
     *  currencyInput: {
     *     currency: "$,£,¢"
     *  }
     */
    $.validator.addMethod("currency", function(value, element, param) {
        var isParamString = typeof param === "string",
            symbol = isParamString ? param : param[0],
            soft = isParamString ? true : param[1],
            regex;

        symbol = symbol.replace(/,/g, "");
        symbol = soft ? symbol + "]" : symbol + "]?";
        regex = "^[" + symbol + "([1-9]{1}[0-9]{0,2}(\\,[0-9]{3})*(\\.[0-9]{0,2})?|[1-9]{1}[0-9]{0,}(\\.[0-9]{0,2})?|0(\\.[0-9]{0,2})?|(\\.[0-9]{1,2})?)$";
        regex = new RegExp(regex);
        return this.optional(element) || regex.test(value);

    }, "Please specify a valid currency");

    $.validator.addMethod("dateFA", function(value, element) {
        return this.optional(element) || /^[1-4]\d{3}\/((0?[1-6]\/((3[0-1])|([1-2][0-9])|(0?[1-9])))|((1[0-2]|(0?[7-9]))\/(30|([1-2][0-9])|(0?[1-9]))))$/.test(value);
    }, $.validator.messages.date);

    /**
     * Return true, if the value is a valid date, also making this formal check dd/mm/yyyy.
     *
     * @example $.validator.methods.date("01/01/1900")
     * @result true
     *
     * @example $.validator.methods.date("01/13/1990")
     * @result false
     *
     * @example $.validator.methods.date("01.01.1900")
     * @result false
     *
     * @example <input name="pippo" class="{dateITA:true}" />
     * @desc Declares an optional input element whose value must be a valid date.
     *
     * @name $.validator.methods.dateITA
     * @type Boolean
     * @cat Plugins/Validate/Methods
     */
    $.validator.addMethod("dateITA", function(value, element) {
        var check = false,
            re = /^\d{1,2}\/\d{1,2}\/\d{4}$/,
            adata, gg, mm, aaaa, xdata;
        if (re.test(value)) {
            adata = value.split("/");
            gg = parseInt(adata[0], 10);
            mm = parseInt(adata[1], 10);
            aaaa = parseInt(adata[2], 10);
            xdata = new Date(Date.UTC(aaaa, mm - 1, gg, 12, 0, 0, 0));
            if ((xdata.getUTCFullYear() === aaaa) && (xdata.getUTCMonth() === mm - 1) && (xdata.getUTCDate() === gg)) {
                check = true;
            } else {
                check = false;
            }
        } else {
            check = false;
        }
        return this.optional(element) || check;
    }, $.validator.messages.date);

    $.validator.addMethod("dateNL", function(value, element) {
        return this.optional(element) || /^(0?[1-9]|[12]\d|3[01])[\.\/\-](0?[1-9]|1[012])[\.\/\-]([12]\d)?(\d\d)$/.test(value);
    }, $.validator.messages.date);

    // Older "accept" file extension method. Old docs: http://docs.jquery.com/Plugins/Validation/Methods/accept
    $.validator.addMethod("extension", function(value, element, param) {
        param = typeof param === "string" ? param.replace(/,/g, "|") : "png|jpe?g|gif";
        return this.optional(element) || value.match(new RegExp("\\.(" + param + ")$", "i"));
    }, $.validator.format("Please enter a value with a valid extension."));

    /**
     * Dutch giro account numbers (not bank numbers) have max 7 digits
     */
    $.validator.addMethod("giroaccountNL", function(value, element) {
        return this.optional(element) || /^[0-9]{1,7}$/.test(value);
    }, "Please specify a valid giro account number");

    /**
     * IBAN is the international bank account number.
     * It has a country - specific format, that is checked here too
     */
    $.validator.addMethod("iban", function(value, element) {
        // some quick simple tests to prevent needless work
        if (this.optional(element)) {
            return true;
        }

        // remove spaces and to upper case
        var iban = value.replace(/ /g, "").toUpperCase(),
            ibancheckdigits = "",
            leadingZeroes = true,
            cRest = "",
            cOperator = "",
            countrycode, ibancheck, charAt, cChar, bbanpattern, bbancountrypatterns, ibanregexp, i, p;

        // check the country code and find the country specific format
        countrycode = iban.substring(0, 2);
        bbancountrypatterns = {
            "AL": "\\d{8}[\\dA-Z]{16}",
            "AD": "\\d{8}[\\dA-Z]{12}",
            "AT": "\\d{16}",
            "AZ": "[\\dA-Z]{4}\\d{20}",
            "BE": "\\d{12}",
            "BH": "[A-Z]{4}[\\dA-Z]{14}",
            "BA": "\\d{16}",
            "BR": "\\d{23}[A-Z][\\dA-Z]",
            "BG": "[A-Z]{4}\\d{6}[\\dA-Z]{8}",
            "CR": "\\d{17}",
            "HR": "\\d{17}",
            "CY": "\\d{8}[\\dA-Z]{16}",
            "CZ": "\\d{20}",
            "DK": "\\d{14}",
            "DO": "[A-Z]{4}\\d{20}",
            "EE": "\\d{16}",
            "FO": "\\d{14}",
            "FI": "\\d{14}",
            "FR": "\\d{10}[\\dA-Z]{11}\\d{2}",
            "GE": "[\\dA-Z]{2}\\d{16}",
            "DE": "\\d{18}",
            "GI": "[A-Z]{4}[\\dA-Z]{15}",
            "GR": "\\d{7}[\\dA-Z]{16}",
            "GL": "\\d{14}",
            "GT": "[\\dA-Z]{4}[\\dA-Z]{20}",
            "HU": "\\d{24}",
            "IS": "\\d{22}",
            "IE": "[\\dA-Z]{4}\\d{14}",
            "IL": "\\d{19}",
            "IT": "[A-Z]\\d{10}[\\dA-Z]{12}",
            "KZ": "\\d{3}[\\dA-Z]{13}",
            "KW": "[A-Z]{4}[\\dA-Z]{22}",
            "LV": "[A-Z]{4}[\\dA-Z]{13}",
            "LB": "\\d{4}[\\dA-Z]{20}",
            "LI": "\\d{5}[\\dA-Z]{12}",
            "LT": "\\d{16}",
            "LU": "\\d{3}[\\dA-Z]{13}",
            "MK": "\\d{3}[\\dA-Z]{10}\\d{2}",
            "MT": "[A-Z]{4}\\d{5}[\\dA-Z]{18}",
            "MR": "\\d{23}",
            "MU": "[A-Z]{4}\\d{19}[A-Z]{3}",
            "MC": "\\d{10}[\\dA-Z]{11}\\d{2}",
            "MD": "[\\dA-Z]{2}\\d{18}",
            "ME": "\\d{18}",
            "NL": "[A-Z]{4}\\d{10}",
            "NO": "\\d{11}",
            "PK": "[\\dA-Z]{4}\\d{16}",
            "PS": "[\\dA-Z]{4}\\d{21}",
            "PL": "\\d{24}",
            "PT": "\\d{21}",
            "RO": "[A-Z]{4}[\\dA-Z]{16}",
            "SM": "[A-Z]\\d{10}[\\dA-Z]{12}",
            "SA": "\\d{2}[\\dA-Z]{18}",
            "RS": "\\d{18}",
            "SK": "\\d{20}",
            "SI": "\\d{15}",
            "ES": "\\d{20}",
            "SE": "\\d{20}",
            "CH": "\\d{5}[\\dA-Z]{12}",
            "TN": "\\d{20}",
            "TR": "\\d{5}[\\dA-Z]{17}",
            "AE": "\\d{3}\\d{16}",
            "GB": "[A-Z]{4}\\d{14}",
            "VG": "[\\dA-Z]{4}\\d{16}"
        };

        bbanpattern = bbancountrypatterns[countrycode];
        // As new countries will start using IBAN in the
        // future, we only check if the countrycode is known.
        // This prevents false negatives, while almost all
        // false positives introduced by this, will be caught
        // by the checksum validation below anyway.
        // Strict checking should return FALSE for unknown
        // countries.
        if (typeof bbanpattern !== "undefined") {
            ibanregexp = new RegExp("^[A-Z]{2}\\d{2}" + bbanpattern + "$", "");
            if (!(ibanregexp.test(iban))) {
                return false; // invalid country specific format
            }
        }

        // now check the checksum, first convert to digits
        ibancheck = iban.substring(4, iban.length) + iban.substring(0, 4);
        for (i = 0; i < ibancheck.length; i++) {
            charAt = ibancheck.charAt(i);
            if (charAt !== "0") {
                leadingZeroes = false;
            }
            if (!leadingZeroes) {
                ibancheckdigits += "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZ".indexOf(charAt);
            }
        }

        // calculate the result of: ibancheckdigits % 97
        for (p = 0; p < ibancheckdigits.length; p++) {
            cChar = ibancheckdigits.charAt(p);
            cOperator = "" + cRest + "" + cChar;
            cRest = cOperator % 97;
        }
        return cRest === 1;
    }, "Please specify a valid IBAN");

    $.validator.addMethod("integer", function(value, element) {
        return this.optional(element) || /^-?\d+$/.test(value);
    }, "A positive or negative non-decimal number please");

    $.validator.addMethod("ipv4", function(value, element) {
        return this.optional(element) || /^(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)\.(25[0-5]|2[0-4]\d|[01]?\d\d?)$/i.test(value);
    }, "Please enter a valid IP v4 address.");

    $.validator.addMethod("ipv6", function(value, element) {
        return this.optional(element) || /^((([0-9A-Fa-f]{1,4}:){7}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}:[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){5}:([0-9A-Fa-f]{1,4}:)?[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){4}:([0-9A-Fa-f]{1,4}:){0,2}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){3}:([0-9A-Fa-f]{1,4}:){0,3}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){2}:([0-9A-Fa-f]{1,4}:){0,4}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){6}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(([0-9A-Fa-f]{1,4}:){0,5}:((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|(::([0-9A-Fa-f]{1,4}:){0,5}((\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b)\.){3}(\b((25[0-5])|(1\d{2})|(2[0-4]\d)|(\d{1,2}))\b))|([0-9A-Fa-f]{1,4}::([0-9A-Fa-f]{1,4}:){0,5}[0-9A-Fa-f]{1,4})|(::([0-9A-Fa-f]{1,4}:){0,6}[0-9A-Fa-f]{1,4})|(([0-9A-Fa-f]{1,4}:){1,7}:))$/i.test(value);
    }, "Please enter a valid IP v6 address.");

    $.validator.addMethod("lettersonly", function(value, element) {
        return this.optional(element) || /^[a-z]+$/i.test(value);
    }, "Letters only please");

    $.validator.addMethod("letterswithbasicpunc", function(value, element) {
        return this.optional(element) || /^[a-z\-.,()'"\s]+$/i.test(value);
    }, "Letters or punctuation only please");

    $.validator.addMethod("mobileNL", function(value, element) {
        return this.optional(element) || /^((\+|00(\s|\s?\-\s?)?)31(\s|\s?\-\s?)?(\(0\)[\-\s]?)?|0)6((\s|\s?\-\s?)?[0-9]){8}$/.test(value);
    }, "Please specify a valid mobile number");

    /* For UK phone functions, do the following server side processing:
     * Compare original input with this RegEx pattern:
     * ^\(?(?:(?:00\)?[\s\-]?\(?|\+)(44)\)?[\s\-]?\(?(?:0\)?[\s\-]?\(?)?|0)([1-9]\d{1,4}\)?[\s\d\-]+)$
     * Extract $1 and set $prefix to '+44<space>' if $1 is '44', otherwise set $prefix to '0'
     * Extract $2 and remove hyphens, spaces and parentheses. Phone number is combined $prefix and $2.
     * A number of very detailed GB telephone number RegEx patterns can also be found at:
     * http://www.aa-asterisk.org.uk/index.php/Regular_Expressions_for_Validating_and_Formatting_GB_Telephone_Numbers
     */
    $.validator.addMethod("mobileUK", function(phone_number, element) {
        phone_number = phone_number.replace(/\(|\)|\s+|-/g, "");
        return this.optional(element) || phone_number.length > 9 &&
            phone_number.match(/^(?:(?:(?:00\s?|\+)44\s?|0)7(?:[1345789]\d{2}|624)\s?\d{3}\s?\d{3})$/);
    }, "Please specify a valid mobile number");

    /*
     * The número de identidad de extranjero ( NIE )is a code used to identify the non-nationals in Spain
     */
    $.validator.addMethod("nieES", function(value) {
        "use strict";

        value = value.toUpperCase();

        // Basic format test
        if (!value.match("((^[A-Z]{1}[0-9]{7}[A-Z0-9]{1}$|^[T]{1}[A-Z0-9]{8}$)|^[0-9]{8}[A-Z]{1}$)")) {
            return false;
        }

        // Test NIE
        //T
        if (/^[T]{1}/.test(value)) {
            return (value[8] === /^[T]{1}[A-Z0-9]{8}$/.test(value));
        }

        //XYZ
        if (/^[XYZ]{1}/.test(value)) {
            return (
                value[8] === "TRWAGMYFPDXBNJZSQVHLCKE".charAt(
                    value.replace("X", "0")
                    .replace("Y", "1")
                    .replace("Z", "2")
                    .substring(0, 8) % 23
                )
            );
        }

        return false;

    }, "Please specify a valid NIE number.");

    /*
     * The Número de Identificación Fiscal ( NIF ) is the way tax identification used in Spain for individuals
     */
    $.validator.addMethod("nifES", function(value) {
        "use strict";

        value = value.toUpperCase();

        // Basic format test
        if (!value.match("((^[A-Z]{1}[0-9]{7}[A-Z0-9]{1}$|^[T]{1}[A-Z0-9]{8}$)|^[0-9]{8}[A-Z]{1}$)")) {
            return false;
        }

        // Test NIF
        if (/^[0-9]{8}[A-Z]{1}$/.test(value)) {
            return ("TRWAGMYFPDXBNJZSQVHLCKE".charAt(value.substring(8, 0) % 23) === value.charAt(8));
        }
        // Test specials NIF (starts with K, L or M)
        if (/^[KLM]{1}/.test(value)) {
            return (value[8] === String.fromCharCode(64));
        }

        return false;

    }, "Please specify a valid NIF number.");

    jQuery.validator.addMethod("notEqualTo", function(value, element, param) {
        return this.optional(element) || !$.validator.methods.equalTo.call(this, value, element, param);
    }, "Please enter a different value, values must not be the same.");

    $.validator.addMethod("nowhitespace", function(value, element) {
        return this.optional(element) || /^\S+$/i.test(value);
    }, "No white space please");

    /**
     * Return true if the field value matches the given format RegExp
     *
     * @example $.validator.methods.pattern("AR1004",element,/^AR\d{4}$/)
     * @result true
     *
     * @example $.validator.methods.pattern("BR1004",element,/^AR\d{4}$/)
     * @result false
     *
     * @name $.validator.methods.pattern
     * @type Boolean
     * @cat Plugins/Validate/Methods
     */
    $.validator.addMethod("pattern", function(value, element, param) {
        if (this.optional(element)) {
            return true;
        }
        if (typeof param === "string") {
            param = new RegExp("^(?:" + param + ")$");
        }
        return param.test(value);
    }, "Invalid format.");

    /**
     * Dutch phone numbers have 10 digits (or 11 and start with +31).
     */
    $.validator.addMethod("phoneNL", function(value, element) {
        return this.optional(element) || /^((\+|00(\s|\s?\-\s?)?)31(\s|\s?\-\s?)?(\(0\)[\-\s]?)?|0)[1-9]((\s|\s?\-\s?)?[0-9]){8}$/.test(value);
    }, "Please specify a valid phone number.");

    /* For UK phone functions, do the following server side processing:
     * Compare original input with this RegEx pattern:
     * ^\(?(?:(?:00\)?[\s\-]?\(?|\+)(44)\)?[\s\-]?\(?(?:0\)?[\s\-]?\(?)?|0)([1-9]\d{1,4}\)?[\s\d\-]+)$
     * Extract $1 and set $prefix to '+44<space>' if $1 is '44', otherwise set $prefix to '0'
     * Extract $2 and remove hyphens, spaces and parentheses. Phone number is combined $prefix and $2.
     * A number of very detailed GB telephone number RegEx patterns can also be found at:
     * http://www.aa-asterisk.org.uk/index.php/Regular_Expressions_for_Validating_and_Formatting_GB_Telephone_Numbers
     */
    $.validator.addMethod("phoneUK", function(phone_number, element) {
        phone_number = phone_number.replace(/\(|\)|\s+|-/g, "");
        return this.optional(element) || phone_number.length > 9 &&
            phone_number.match(/^(?:(?:(?:00\s?|\+)44\s?)|(?:\(?0))(?:\d{2}\)?\s?\d{4}\s?\d{4}|\d{3}\)?\s?\d{3}\s?\d{3,4}|\d{4}\)?\s?(?:\d{5}|\d{3}\s?\d{3})|\d{5}\)?\s?\d{4,5})$/);
    }, "Please specify a valid phone number");

    /**
     * matches US phone number format
     *
     * where the area code may not start with 1 and the prefix may not start with 1
     * allows '-' or ' ' as a separator and allows parens around area code
     * some people may want to put a '1' in front of their number
     *
     * 1(212)-999-2345 or
     * 212 999 2344 or
     * 212-999-0983
     *
     * but not
     * 111-123-5434
     * and not
     * 212 123 4567
     */
    $.validator.addMethod("phoneUS", function(phone_number, element) {
        phone_number = phone_number.replace(/\s+/g, "");
        return this.optional(element) || phone_number.length > 9 &&
            phone_number.match(/^(\+?1-?)?(\([2-9]([02-9]\d|1[02-9])\)|[2-9]([02-9]\d|1[02-9]))-?[2-9]([02-9]\d|1[02-9])-?\d{4}$/);
    }, "Please specify a valid phone number");

    /* For UK phone functions, do the following server side processing:
     * Compare original input with this RegEx pattern:
     * ^\(?(?:(?:00\)?[\s\-]?\(?|\+)(44)\)?[\s\-]?\(?(?:0\)?[\s\-]?\(?)?|0)([1-9]\d{1,4}\)?[\s\d\-]+)$
     * Extract $1 and set $prefix to '+44<space>' if $1 is '44', otherwise set $prefix to '0'
     * Extract $2 and remove hyphens, spaces and parentheses. Phone number is combined $prefix and $2.
     * A number of very detailed GB telephone number RegEx patterns can also be found at:
     * http://www.aa-asterisk.org.uk/index.php/Regular_Expressions_for_Validating_and_Formatting_GB_Telephone_Numbers
     */
    //Matches UK landline + mobile, accepting only 01-3 for landline or 07 for mobile to exclude many premium numbers
    $.validator.addMethod("phonesUK", function(phone_number, element) {
        phone_number = phone_number.replace(/\(|\)|\s+|-/g, "");
        return this.optional(element) || phone_number.length > 9 &&
            phone_number.match(/^(?:(?:(?:00\s?|\+)44\s?|0)(?:1\d{8,9}|[23]\d{9}|7(?:[1345789]\d{8}|624\d{6})))$/);
    }, "Please specify a valid uk phone number");

    /**
     * Matches a valid Canadian Postal Code
     *
     * @example jQuery.validator.methods.postalCodeCA( "H0H 0H0", element )
     * @result true
     *
     * @example jQuery.validator.methods.postalCodeCA( "H0H0H0", element )
     * @result false
     *
     * @name jQuery.validator.methods.postalCodeCA
     * @type Boolean
     * @cat Plugins/Validate/Methods
     */
    $.validator.addMethod("postalCodeCA", function(value, element) {
        return this.optional(element) || /^[ABCEGHJKLMNPRSTVXY]\d[A-Z] \d[A-Z]\d$/.test(value);
    }, "Please specify a valid postal code");

    /*
     * Valida CEPs do brasileiros:
     *
     * Formatos aceitos:
     * 99999-999
     * 99.999-999
     * 99999999
     */
    $.validator.addMethod("postalcodeBR", function(cep_value, element) {
        return this.optional(element) || /^\d{2}.\d{3}-\d{3}?$|^\d{5}-?\d{3}?$/.test(cep_value);
    }, "Informe um CEP válido.");

    /* Matches Italian postcode (CAP) */
    $.validator.addMethod("postalcodeIT", function(value, element) {
        return this.optional(element) || /^\d{5}$/.test(value);
    }, "Please specify a valid postal code");

    $.validator.addMethod("postalcodeNL", function(value, element) {
        return this.optional(element) || /^[1-9][0-9]{3}\s?[a-zA-Z]{2}$/.test(value);
    }, "Please specify a valid postal code");

    // Matches UK postcode. Does not match to UK Channel Islands that have their own postcodes (non standard UK)
    $.validator.addMethod("postcodeUK", function(value, element) {
        return this.optional(element) || /^((([A-PR-UWYZ][0-9])|([A-PR-UWYZ][0-9][0-9])|([A-PR-UWYZ][A-HK-Y][0-9])|([A-PR-UWYZ][A-HK-Y][0-9][0-9])|([A-PR-UWYZ][0-9][A-HJKSTUW])|([A-PR-UWYZ][A-HK-Y][0-9][ABEHMNPRVWXY]))\s?([0-9][ABD-HJLNP-UW-Z]{2})|(GIR)\s?(0AA))$/i.test(value);
    }, "Please specify a valid UK postcode");

    /*
     * Lets you say "at least X inputs that match selector Y must be filled."
     *
     * The end result is that neither of these inputs:
     *
     *	<input class="productinfo" name="partnumber">
     *	<input class="productinfo" name="description">
     *
     *	...will validate unless at least one of them is filled.
     *
     * partnumber:	{require_from_group: [1,".productinfo"]},
     * description: {require_from_group: [1,".productinfo"]}
     *
     * options[0]: number of fields that must be filled in the group
     * options[1]: CSS selector that defines the group of conditionally required fields
     */
    $.validator.addMethod("require_from_group", function(value, element, options) {
        var $fields = $(options[1], element.form),
            $fieldsFirst = $fields.eq(0),
            validator = $fieldsFirst.data("valid_req_grp") ? $fieldsFirst.data("valid_req_grp") : $.extend({}, this),
            isValid = $fields.filter(function() {
                return validator.elementValue(this);
            }).length >= options[0];

        // Store the cloned validator for future validation
        $fieldsFirst.data("valid_req_grp", validator);

        // If element isn't being validated, run each require_from_group field's validation rules
        if (!$(element).data("being_validated")) {
            $fields.data("being_validated", true);
            $fields.each(function() {
                validator.element(this);
            });
            $fields.data("being_validated", false);
        }
        return isValid;
    }, $.validator.format("Please fill at least {0} of these fields."));

    /*
     * Lets you say "either at least X inputs that match selector Y must be filled,
     * OR they must all be skipped (left blank)."
     *
     * The end result, is that none of these inputs:
     *
     *	<input class="productinfo" name="partnumber">
     *	<input class="productinfo" name="description">
     *	<input class="productinfo" name="color">
     *
     *	...will validate unless either at least two of them are filled,
     *	OR none of them are.
     *
     * partnumber:	{skip_or_fill_minimum: [2,".productinfo"]},
     * description: {skip_or_fill_minimum: [2,".productinfo"]},
     * color:		{skip_or_fill_minimum: [2,".productinfo"]}
     *
     * options[0]: number of fields that must be filled in the group
     * options[1]: CSS selector that defines the group of conditionally required fields
     *
     */
    $.validator.addMethod("skip_or_fill_minimum", function(value, element, options) {
        var $fields = $(options[1], element.form),
            $fieldsFirst = $fields.eq(0),
            validator = $fieldsFirst.data("valid_skip") ? $fieldsFirst.data("valid_skip") : $.extend({}, this),
            numberFilled = $fields.filter(function() {
                return validator.elementValue(this);
            }).length,
            isValid = numberFilled === 0 || numberFilled >= options[0];

        // Store the cloned validator for future validation
        $fieldsFirst.data("valid_skip", validator);

        // If element isn't being validated, run each skip_or_fill_minimum field's validation rules
        if (!$(element).data("being_validated")) {
            $fields.data("being_validated", true);
            $fields.each(function() {
                validator.element(this);
            });
            $fields.data("being_validated", false);
        }
        return isValid;
    }, $.validator.format("Please either skip these fields or fill at least {0} of them."));

    /* Validates US States and/or Territories by @jdforsythe
     * Can be case insensitive or require capitalization - default is case insensitive
     * Can include US Territories or not - default does not
     * Can include US Military postal abbreviations (AA, AE, AP) - default does not
     *
     * Note: "States" always includes DC (District of Colombia)
     *
     * Usage examples:
     *
     *  This is the default - case insensitive, no territories, no military zones
     *  stateInput: {
     *     caseSensitive: false,
     *     includeTerritories: false,
     *     includeMilitary: false
     *  }
     *
     *  Only allow capital letters, no territories, no military zones
     *  stateInput: {
     *     caseSensitive: false
     *  }
     *
     *  Case insensitive, include territories but not military zones
     *  stateInput: {
     *     includeTerritories: true
     *  }
     *
     *  Only allow capital letters, include territories and military zones
     *  stateInput: {
     *     caseSensitive: true,
     *     includeTerritories: true,
     *     includeMilitary: true
     *  }
     *
     *
     *
     */

    $.validator.addMethod("stateUS", function(value, element, options) {
            var isDefault = typeof options === "undefined",
                caseSensitive = (isDefault || typeof options.caseSensitive === "undefined") ? false : options.caseSensitive,
                includeTerritories = (isDefault || typeof options.includeTerritories === "undefined") ? false : options.includeTerritories,
                includeMilitary = (isDefault || typeof options.includeMilitary === "undefined") ? false : options.includeMilitary,
                regex;

            if (!includeTerritories && !includeMilitary) {
                regex = "^(A[KLRZ]|C[AOT]|D[CE]|FL|GA|HI|I[ADLN]|K[SY]|LA|M[ADEINOST]|N[CDEHJMVY]|O[HKR]|PA|RI|S[CD]|T[NX]|UT|V[AT]|W[AIVY])$";
            } else if (includeTerritories && includeMilitary) {
                regex = "^(A[AEKLPRSZ]|C[AOT]|D[CE]|FL|G[AU]|HI|I[ADLN]|K[SY]|LA|M[ADEINOPST]|N[CDEHJMVY]|O[HKR]|P[AR]|RI|S[CD]|T[NX]|UT|V[AIT]|W[AIVY])$";
            } else if (includeTerritories) {
                regex = "^(A[KLRSZ]|C[AOT]|D[CE]|FL|G[AU]|HI|I[ADLN]|K[SY]|LA|M[ADEINOPST]|N[CDEHJMVY]|O[HKR]|P[AR]|RI|S[CD]|T[NX]|UT|V[AIT]|W[AIVY])$";
            } else {
                regex = "^(A[AEKLPRZ]|C[AOT]|D[CE]|FL|GA|HI|I[ADLN]|K[SY]|LA|M[ADEINOST]|N[CDEHJMVY]|O[HKR]|PA|RI|S[CD]|T[NX]|UT|V[AT]|W[AIVY])$";
            }

            regex = caseSensitive ? new RegExp(regex) : new RegExp(regex, "i");
            return this.optional(element) || regex.test(value);
        },
        "Please specify a valid state");

    // TODO check if value starts with <, otherwise don't try stripping anything
    $.validator.addMethod("strippedminlength", function(value, element, param) {
        return $(value).text().length >= param;
    }, $.validator.format("Please enter at least {0} characters"));

    $.validator.addMethod("time", function(value, element) {
        return this.optional(element) || /^([01]\d|2[0-3]|[0-9])(:[0-5]\d){1,2}$/.test(value);
    }, "Please enter a valid time, between 00:00 and 23:59");

    $.validator.addMethod("time12h", function(value, element) {
        return this.optional(element) || /^((0?[1-9]|1[012])(:[0-5]\d){1,2}(\ ?[AP]M))$/i.test(value);
    }, "Please enter a valid time in 12-hour am/pm format");

    // same as url, but TLD is optional
    $.validator.addMethod("url2", function(value, element) {
        return this.optional(element) || /^(https?|ftp):\/\/(((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:)*@)?(((\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5])\.(\d|[1-9]\d|1\d\d|2[0-4]\d|25[0-5]))|((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)*(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?)(:\d*)?)(\/((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)+(\/(([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)*)*)?)?(\?((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|[\uE000-\uF8FF]|\/|\?)*)?(#((([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(%[\da-f]{2})|[!\$&'\(\)\*\+,;=]|:|@)|\/|\?)*)?$/i.test(value);
    }, $.validator.messages.url);

    /**
     * Return true, if the value is a valid vehicle identification number (VIN).
     *
     * Works with all kind of text inputs.
     *
     * @example <input type="text" size="20" name="VehicleID" class="{required:true,vinUS:true}" />
     * @desc Declares a required input element whose value must be a valid vehicle identification number.
     *
     * @name $.validator.methods.vinUS
     * @type Boolean
     * @cat Plugins/Validate/Methods
     */
    $.validator.addMethod("vinUS", function(v) {
        if (v.length !== 17) {
            return false;
        }

        var LL = ["A", "B", "C", "D", "E", "F", "G", "H", "J", "K", "L", "M", "N", "P", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"],
            VL = [1, 2, 3, 4, 5, 6, 7, 8, 1, 2, 3, 4, 5, 7, 9, 2, 3, 4, 5, 6, 7, 8, 9],
            FL = [8, 7, 6, 5, 4, 3, 2, 10, 0, 9, 8, 7, 6, 5, 4, 3, 2],
            rs = 0,
            i, n, d, f, cd, cdv;

        for (i = 0; i < 17; i++) {
            f = FL[i];
            d = v.slice(i, i + 1);
            if (i === 8) {
                cdv = d;
            }
            if (!isNaN(d)) {
                d *= f;
            } else {
                for (n = 0; n < LL.length; n++) {
                    if (d.toUpperCase() === LL[n]) {
                        d = VL[n];
                        d *= f;
                        if (isNaN(cdv) && n === 8) {
                            cdv = LL[n];
                        }
                        break;
                    }
                }
            }
            rs += d;
        }
        cd = rs % 11;
        if (cd === 10) {
            cd = "X";
        }
        if (cd === cdv) {
            return true;
        }
        return false;
    }, "The specified vehicle identification number (VIN) is invalid.");

    $.validator.addMethod("zipcodeUS", function(value, element) {
        return this.optional(element) || /^\d{5}(-\d{4})?$/.test(value);
    }, "The specified US ZIP Code is invalid");

    $.validator.addMethod("ziprange", function(value, element) {
        return this.optional(element) || /^90[2-5]\d\{2\}-\d{4}$/.test(value);
    }, "Your ZIP-code must be in the range 902xx-xxxx to 905xx-xxxx");

}));

/*!
 * Custom validation rules for WVN
 */

(function(factory) {
    if (typeof define === "function" && define.amd) {
        define(["static/frontend/js/jquery", "./jquery.validate"], factory);
    } else {
        factory(jQuery);
    }
}(function($) {

    // add method for checking valid phone
    var pattern = /(^\+[0-9]{2}|^\+[0-9]{2}\(0\)|^\(\+[0-9]{2}\)\(0\)|^00[0-9]{2}|^0)([0-9]{9}$|[0-9\s]{7,11}$)/;
    $.validator.addMethod('phone', function(value, element) {
        return this.optional(element) || pattern.test(value);
    }, 'Please enter a valid phone number.');

    // formated numeric check
    $.validator.addMethod('number_min', function(value, element, params) {
        var source = $.isNumeric(value) ? value : parseInt(value.replace(/\D+/g, ''), 10),
            target = $.isNumeric(params[0]) ? params[0] : parseInt(params[0].replace(/\D+/g, ''), 10);
            
        return source >= target;
    }, $.validator.format('Please input a value higher than {0}.'));

}));

(function(factory) {
    if (typeof define === "function" && define.amd) {
        define(["static/frontend/js/jquery", "../jquery.validate"], factory);
    } else {
        factory(jQuery);
    }
}(function($) {
    if (WVN.language != 'en') return;

    /*
     * Translated default messages for the jQuery validation plugin.
     * Locale: EN
     */
    $.extend($.validator.messages, {
        required: "This field is required.",
        remote: "Please fix this field.",
        email: "Please enter a valid email address.",
        url: "Please enter a valid URL.",
        date: "Please enter a valid date.",
        dateISO: "Please enter a valid date ( ISO ).",
        number: "Please enter a valid number.",
        digits: "Please enter only digits.",
        creditcard: "Please enter a valid credit card number.",
        equalTo: "Please enter the same value again.",
        maxlength: $.validator.format("Please enter no more than {0} characters."),
        minlength: $.validator.format("Please enter at least {0} characters."),
        rangelength: $.validator.format("Please enter a value between {0} and {1} characters long."),
        range: $.validator.format("Please enter a value between {0} and {1}."),
        max: $.validator.format("Please enter a value less than or equal to {0}."),
        min: $.validator.format("Please enter a value greater than or equal to {0}."),
        email_or_phone: "Please enter a valid phone or email address.",
    });

}));

(function(factory) {
    if (typeof define === "function" && define.amd) {
        define(["static/frontend/js/jquery", "../jquery.validate"], factory);
    } else {
        factory(jQuery);
    }
}(function($) {
    if (WVN.language != 'vi') return;

    /*
     * Translated default messages for the jQuery validation plugin.
     * Locale: VI (Vietnamese; Tiếng Việt)
     */
    $.extend($.validator.messages, {
        required: "Vui lòng nhập.",
        remote: "Vui lòng sửa cho đúng.",
        email: "Vui lòng nhập email.",
        url: "Vui lòng nhập URL.",
        date: "Vui lòng nhập ngày.",
        dateISO: "Vui lòng nhập ngày (ISO).",
        number: "Vui lòng nhập số.",
        digits: "Vui lòng nhập chữ số.",
        creditcard: "Vui lòng nhập số thẻ tín dụng.",
        equalTo: "Vui lòng nhập thêm lần nữa.",
        extension: "Phần mở rộng không đúng.",
        maxlength: $.validator.format("Vui lòng nhập từ {0} kí tự trở xuống."),
        minlength: $.validator.format("Vui lòng nhập từ {0} kí tự trở lên."),
        rangelength: $.validator.format("Vui lòng nhập từ {0} đến {1} kí tự."),
        range: $.validator.format("Vui lòng nhập từ {0} đến {1}."),
        max: $.validator.format("Vui lòng nhập từ {0} trở xuống."),
        min: $.validator.format("Vui lòng nhập từ {0} trở lên."),
        phone: "Vui lòng nhập số điện thoại.",
        number_min: $.validator.format("Vui lòng nhập từ {0} trở lên."),
        email_or_phone: "Vui lòng nhập số điện thoại hoặc email.",
    });

}));

//# sourceMappingURL=jquery_libs.js.map
