    AccountKit_OnInteractive = function() {
        AccountKit.init({
            appId: FB_APP_ID,
            state: document.getElementsByName('_token')[0].value,
            version: FB_ACK_VER,
            fbAppEventsEnabled: true
        });
    };
