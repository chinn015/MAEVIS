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
    const userID = fromNotifResult.val().notifiedTo;
    const reportID = fromNotifResult.val().notifReportID;
    const notifTitle = fromNotifResult.val().notifTitle;
    const notifMessage = fromNotifResult.val().notifMessage;

    console.log('Send notification to: ', userID);

    // const user_query = admin.database().ref()
    const deviceToken = admin.database().ref(`/Users/${userID}/deviceToken`).once('value');
    return deviceToken.then(result => {
      const tokenID = result.val();
      console.log('Device Token: ', tokenID);

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
  });


});
