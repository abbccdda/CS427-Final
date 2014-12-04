INSERT INTO patients
(MID, 
firstName,
lastName, 
email,
address1,
address2,
city,
state,
zip,
phone,
eName,
ePhone,
iCName,
iCAddress1,
iCAddress2,
iCCity, 
ICState,
iCZip,
iCPhone,
iCID,
DateOfBirth,
dateofdeath,
causeofdeath,
MotherMID,
FatherMID,
BloodType,
Ethnicity,
Gender,
TopicalNotes
)
VALUES (
63,
'Jenny',
'Craig',
'fryp@fakencsu.edu',
'2870 Gorgon Drive',
'555',
'Raleigh',
'NC',
'27603',
'525-455-5654',
'Jumbo the Clown',
'515-551-5551',
'IC2',
'Street3',
'Street4',
'Downtown',
'GA',
'19023-2735',
'559-595-5995',
'4',
'1986-2-15',
'2004-01-01',
'84.50',
2,
1,
'O-',
'Caucasian',
'Female',
"Crispy"
)  ON DUPLICATE KEY UPDATE MID = MID;

INSERT INTO users(MID, password, role, sQuestion, sAnswer) 
			VALUES (26, '30c952fab122c3f9759f02a6d95c3758b246b4fee239957b2d4fee46e26170c4', 'patient', 'how you doin?', 'good')
 ON DUPLICATE KEY UPDATE MID = MID;
 /*password: pw*/

insert into obstetrics(MID, yearOfConception, weeksPregnant, hoursLabor, deliveryMethod)
values(63, 2012, '08-4', 3.5, 'Vaginal Delivery');

insert into obstetrics(MID, yearOfConception, weeksPregnant, hoursLabor, deliveryMethod)
values(63, 2012, '01-7', 2.5, 'Vaginal Delivery');