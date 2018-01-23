'use strict'

const functions = require('firebase-functions');
const admin = require('firebase-admin');
admin.initializeApp(functions.config().firebase);

exports.sendNotification = functions.database.ref('/Notifications/{notifID}/').onWrite(event => {
  const notifID = event.params.notifID;

  console.log('The notif id is: ', notifID);

  if(!event.data.val()) {
    return console.log("A notification has been deleted from the database: ", notifID);
  }

  const notifItem = admin.database().ref(`/Notifications/${notifID}`).once('value');
  return notifItem.then(fromNotifResult => {
    const userID = fromNotifResult.val().notifiedTo;;
    //const notifiedToSize = userID.length;
    const reportID = fromNotifResult.val().notifReportID;
    const notifTitle = fromNotifResult.val().notifTitle;
    const notifMessage = fromNotifResult.val().notifMessage;

    //console.log('NotifiedTo SIZE!!!!!');
    //console.log('NotifiedTo SIZE: ', notifiedToSize);
    console.log('Send notification to: ', userID);

    /*for(var i = 0; i < notifiedToSize; i++) {
      console.log('[loop] User ID to send to: ' + i, userID[i]);
    }*/
    // const user_query = admin.database().ref()
    //for(var i = 1; i < notifiedToSize; i++) {
      //console.log('[outside loop] User ID to send to: ', userID[i]);

      var deviceToken = admin.database().ref(`/Users/${userID}/deviceToken`).once('value');
      return deviceToken.then(result => {
        var tokenID = result.val();
        console.log('User ID to send to: ', userID);
        console.log('Device Token of user: ', tokenID);

        const payload = {
          notification: {
            title: `${notifTitle}`,
            body: `${notifMessage}`
          }
        }

        return admin.messaging().sendToDevice(tokenID, payload).then(response => {
          console.log("This was a notification feature.");
        });

      });
    //}
  });


});
