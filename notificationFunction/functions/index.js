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
  return notifItem.then(fromUserResult => {
    const userID = fromUserResult.val().notifiedTo;
    console.log('Send notification to: ', userID);

    const deviceToken = admin.database().ref(`/Users/${userID}/deviceToken`).once('value');
    return deviceToken.then(result => {
      const tokenID = result.val();
      console.log('Device Token: ', tokenID);

      const payload = {
        notification: {
          title: "Vehicular Accident Report",
          body: "Someone reported a Vehicular Accident at somewhere."
        }
      }

      return admin.messaging().sendToDevice(tokenID, payload).then(response => {
        console.log("This was a notification feature.");
      });

    });
  });


});
