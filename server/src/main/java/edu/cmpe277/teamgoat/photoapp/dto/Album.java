package edu.cmpe277.teamgoat.photoapp.dto;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.*;

@Document
//@JsonIgnoreProperties({"_ID"})
@CompoundIndexes({
        @CompoundIndex(name = "unique_album_idx", def = "{'name': 1, 'ownerId': 1}")
})
public class Album {

    @Id
    private String _ID;

    private String name;

    private String description;

    @Indexed
    private String ownerId;

	@Indexed
    private List<String> grantedUserIds;

    private boolean isPubliclyAccessible = false;

    private String coverPhotoUrl;

    @DBRef
    @CascadeSave
    private List<Image> images;

    public Album() {

    }

    public Album(String name, String ownerId, String description, List<String> grantedUserIds, List<Image> images, boolean isPubliclyAccessible) {
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.grantedUserIds = grantedUserIds;
        this.images = images;
        this.isPubliclyAccessible = isPubliclyAccessible;
        ensureGrantedUserIdsUnique();
        ensureOwnerIsGrantedAccessToOwnAlbum();
    }

    public Album(String _ID, String name, String ownerId, String description, List<String> grantedUserIds, List<Image> images, boolean isPubliclyAccessible) {
        this._ID = _ID;
        this.name = name;
        this.description = description;
        this.ownerId = ownerId;
        this.grantedUserIds = grantedUserIds;
        this.images = images;
        this.isPubliclyAccessible = isPubliclyAccessible;
        ensureGrantedUserIdsUnique();
        ensureOwnerIsGrantedAccessToOwnAlbum();
    }

    public String get_ID() {
        return _ID;
    }

    public void set_ID(String _ID) {
        this._ID = _ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public void setOwnerId(String ownerId) {
        this.ownerId = ownerId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPubliclyAccessible() {
        return isPubliclyAccessible;
    }

    public void setIsPubliclyAccessible(boolean isPubliclyAccessible) {
        this.isPubliclyAccessible = isPubliclyAccessible;
    }

    public List<String> getGrantedUserIds() {
        return Collections.unmodifiableList(grantedUserIds);
    }

    public void setGrantedUserIds(List<String> grantedUserIds) {
        this.grantedUserIds = new ArrayList<>(grantedUserIds);
        ensureGrantedUserIdsUnique();
        ensureOwnerIsGrantedAccessToOwnAlbum();
    }

    public void addGrantedUserId(String userId) {
        grantedUserIds.add(userId);
        ensureGrantedUserIdsUnique();
        ensureOwnerIsGrantedAccessToOwnAlbum();
    }

    private void ensureGrantedUserIdsUnique() {
        Set<String> set = new HashSet<>(grantedUserIds);
        grantedUserIds = new ArrayList<>(Arrays.asList(set.toArray(new String[]{})));
    }

    private void ensureImagesUnique() {
        Set<Image> set = new HashSet<>(images);
        images = new ArrayList<>(Arrays.asList(set.toArray(new Image[]{})));
    }

    private void ensureOwnerIsGrantedAccessToOwnAlbum() {
        if (!grantedUserIds.contains(ownerId)) {
            grantedUserIds.add(ownerId);
        }
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = new ArrayList<>(images);
        ensureImagesUnique();
    }

    public void addImage(Image image) {
        this.images.add(image);
        ensureImagesUnique();
    }

    public String getCoverPhotoUrl() {
        return coverPhotoUrl;
    }

    public void setCoverPhotoUrl(String coverPhotoUrl) {
        this.coverPhotoUrl = coverPhotoUrl;
    }

    //    @Override
//    public String toString() {
//        return "Album{" +
//                "_ID='" + _ID + '\'' +
//                ", name='" + name + '\'' +
//                ", ownerId='" + ownerId + '\'' +
//                ", grantedUserIds='" + Arrays.toString(grantedUserIds) + '\'' +
//                ", images=" + Arrays.toString(images) +
//                '}';
//    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Album album = (Album) o;

        return !(_ID != null ? !_ID.equals(album._ID) : album._ID != null);

    }

    @Override
    public int hashCode() {
        return _ID != null ? _ID.hashCode() : 0;
    }
}
