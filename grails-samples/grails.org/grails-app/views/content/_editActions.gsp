<div id="editLinks" style="margin-left:400px;">

    <a class="actionIcon" onclick="Effect.Appear('uploadDialog')">
        <img src="${createLinkTo(dir:'images/','icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0" />
    </a>
    <a href="#" onclick="Effect.Appear('uploadDialog')">Upload Image</a>&nbsp;&nbsp;

    <a class="actionIcon" onclick="new Ajax.Updater('contentPane','${createLink(controller:'content',action:'saveWikiPage',id:content?.title)}',getAjaxOptions());return false">
        <img src="${createLinkTo(dir:'images/','icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0" />
    </a>
    <a href="#" onclick="new Ajax.Updater('contentPane','${createLink(controller:'content',action:'saveWikiPage',id:content?.title)}',getAjaxOptions());return false">Save</a>&nbsp;&nbsp;

    <g:remoteLink class="actionIcon" update="contentPane" controller="content" id="${content?.title}" params="[xhr:true]"><img src="${createLinkTo(dir:'images/','icon-edit.png')}" width="15" height="15" alt="Icon Edit" class="inlineIcon" border="0" /></g:remoteLink>
    <g:remoteLink update="contentPane" controller="content" id="${content?.title}" params="[xhr:true]">Cancel</g:remoteLink>
</div>