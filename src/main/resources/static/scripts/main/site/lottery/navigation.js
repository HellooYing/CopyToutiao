function ShowActivityPage(){
    document.getElementById('activity-page').style.display="block";
    document.getElementById('prize-page').style.display="none";
    document.getElementById('personal-info-page').style.display="none";
}

function ShowPrizePage(){
    document.getElementById('prize-page').style.display="block";
    document.getElementById('activity-page').style.display="none";
    document.getElementById('personal-info-page').style.display="none";
}

function ShowSettingsPage(){
    document.getElementById('personal-info-page').style.display="block";
    document.getElementById('prize-page').style.display="none";
    document.getElementById('activity-page').style.display="none";
}