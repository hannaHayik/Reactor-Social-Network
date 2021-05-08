package bgu.spl.net.api.bidi.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("unused")
public class DataBase {
	private ConcurrentHashMap<Integer, String> isOnline;
	private ConcurrentHashMap<String, String> usersInfo;
	private ConcurrentHashMap<String, List<String>> followersMap;
	private ConcurrentHashMap<String, List<String>> whoIsFollowingMe;
	private ConcurrentHashMap<String, List<String>> messagesMap;
	private List<String> registeredUsersByDate;
	private ConcurrentHashMap<String, List<String>> messagesWhileOffline;
	private ConcurrentHashMap<String, List<String>> postsWhileOffline;
	private ConcurrentHashMap<String, List<String>> postsMap;

	public DataBase() {
		this.usersInfo = new ConcurrentHashMap<String, String>();
		this.followersMap = new ConcurrentHashMap<String, List<String>>();
		this.isOnline = new ConcurrentHashMap<Integer, String>();
		this.messagesMap = new ConcurrentHashMap<String, List<String>>();
		this.registeredUsersByDate = new ArrayList<String>();
		this.whoIsFollowingMe = new ConcurrentHashMap<String, List<String>>();
		this.messagesWhileOffline = new ConcurrentHashMap<String, List<String>>();
		this.postsWhileOffline = new ConcurrentHashMap<String, List<String>>();
		this.postsMap = new ConcurrentHashMap<String, List<String>>();
	}

	// If user offline, this  hashmap saves all PM sent to him while offline.
	public void addMessageToUser(String message, String name, String sender) {
		synchronized (this.messagesWhileOffline) {
			if (this.messagesWhileOffline.containsKey(name)) {
				this.messagesWhileOffline.get(name).add(sender);
				this.messagesWhileOffline.get(name).add(message);
			} else {
				this.messagesWhileOffline.put(name, new ArrayList<String>());
				this.messagesWhileOffline.get(name).add(sender);
				this.messagesWhileOffline.get(name).add(message);
			}
		}
	}

	// To count posts later in STAT command.
	public void addPostToSystem(String name, String postToAdd) {
		synchronized (this.postsMap) {
			if (!this.postsMap.containsKey(name)) {
				this.postsMap.put(name, new ArrayList<String>());
				this.postsMap.get(name).add(postToAdd);
			} else
				this.postsMap.get(name).add(postToAdd);
		}
	}

	public int NumberOfPosts(int id) {
		if (this.postsMap.containsKey(this.getUserName(id))) {
			return this.postsMap.get(this.getUserName(id)).size();
		}
		return 0;
	}

	// Two version of this function, one according to the connection number and the
	// other according to name.
	public int NumberOfPosts(String name) {
		if (this.postsMap.containsKey(name)) {
			return this.postsMap.get(name).size();
		}
		return 0;
	}

	public void addPostToUser(String post, String name, String sender) {
		synchronized (this.postsWhileOffline) {
			if (this.postsWhileOffline.containsKey(name)) {
				this.postsWhileOffline.get(name).add(sender);
				this.postsWhileOffline.get(name).add(post);
			} else {
				this.postsWhileOffline.put(name, new ArrayList<String>());
				this.postsWhileOffline.get(name).add(sender);
				this.postsWhileOffline.get(name).add(post);
			}
		}

	}

	// Return messages received while offline, put an empty list for the next time.
	public List<String> getOfflineMessages(String name) {
		synchronized (this.messagesWhileOffline) {
			if (this.messagesWhileOffline.get(name) == null) {
				this.messagesWhileOffline.put(name, new ArrayList<String>());
				return this.messagesWhileOffline.get(name);
			} else {
				List<String> toReturn = this.messagesWhileOffline.get(name);
				this.messagesWhileOffline.put(name, new ArrayList<String>());
				return toReturn;
			}
		}
	}

	public List<String> getOfflinePosts(String name) {
		synchronized (this.postsWhileOffline) {
			if (this.postsWhileOffline.get(name) == null) {
				this.postsWhileOffline.put(name, new ArrayList<String>());
				return this.postsWhileOffline.get(name);
			} else {
				List<String> toReturn = this.postsWhileOffline.get(name);
				this.postsWhileOffline.put(name, new ArrayList<String>());
				return toReturn;
			}
		}
	}

	public int NumberOfFollwers(int id) {
		if (this.whoIsFollowingMe.containsKey(this.getUserName(id))) {
			return this.whoIsFollowingMe.get(this.getUserName(id)).size();
		}

		return 0;
	}

	public int NumberOfFollwers(String name) {
		if (this.whoIsFollowingMe.containsKey(name)) {
			return this.whoIsFollowingMe.get(name).size();
		}

		return 0;
	}

	public int NumberOfFollowing(int id) {
		if (this.followersMap.containsKey(this.getUserName(id))) {
			return this.followersMap.get(this.getUserName(id)).size();
		}
		return 0;
	}

	public int NumberOfFollowing(String name) {
		if (this.followersMap.containsKey(name)) {
			return this.followersMap.get(name).size();
		}
		return 0;
	}

	public boolean registerUser(String name, String password) {
		synchronized (this.usersInfo) {
			if (!usersInfo.containsKey(name)) {
				this.usersInfo.put(name, password);
				synchronized (this.registeredUsersByDate) {
					this.registeredUsersByDate.add(name);
				}
				return true;
			}
			return false;
		}
	}

	public List<String> followUser(String name, List<String> toFollow) {
		synchronized (this.followersMap) {
			List<String> toReturnList = new ArrayList<String>();
			for (int i = 0; i < toFollow.size(); i++) {
				if (!followersMap.containsKey(name)) {
					this.followersMap.put(name, new ArrayList<String>());
					if (this.checkIfExists(toFollow.get(i))) {
						this.followersMap.get(name).add(toFollow.get(i));
						toReturnList.add(toFollow.get(i));
						this.updateFollowers(toFollow.get(i), name);
					}
				} else {
					if ((!followersMap.get(name).contains(toFollow.get(i))) && this.checkIfExists(toFollow.get(i))) {
						followersMap.get(name).add(toFollow.get(i));
						toReturnList.add(toFollow.get(i));
						this.updateFollowers(toFollow.get(i), name);
					}
				}
			}
			return toReturnList;
		}
	}

	// No need to synchronize here because it's used by a synchronized function only
	// and it's a private function.
	private void updateFollowers(String me, String nameThatFollowMe) {
		if (!this.whoIsFollowingMe.containsKey(me)) {
			this.whoIsFollowingMe.put(me, new ArrayList<String>());
			this.whoIsFollowingMe.get(me).add(nameThatFollowMe);
		} else {
			if (!this.whoIsFollowingMe.get(me).contains(nameThatFollowMe))
				this.whoIsFollowingMe.get(me).add(nameThatFollowMe);
		}
	}

	public List<String> unfollowUser(String name, List<String> toUnFollow) {
		synchronized (this.followersMap) {
			List<String> toReturnList = new ArrayList<String>();
			for (int i = 0; i < toUnFollow.size(); i++) {
				if (followersMap.containsKey(name)) {
					if (this.followersMap.get(name).remove(toUnFollow.get(i))) {
						toReturnList.add(toUnFollow.get(i));
						this.updateUnFollowers(toUnFollow.get(i), name);
					}
				}
			}
			return toReturnList;
		}
	}

	private void updateUnFollowers(String me, String nameToUnfollowMe) {
		if (this.whoIsFollowingMe.containsKey(me)) {
			this.whoIsFollowingMe.get(me).remove(nameToUnfollowMe);
		}
	}

	public void addMessageToSystem(String msg, int connectionId) {
		synchronized (this.messagesMap) {
			if (!this.messagesMap.containsKey(this.getUserName(connectionId))) {
				this.messagesMap.put(this.getUserName(connectionId), new ArrayList<String>());
				this.messagesMap.get(this.getUserName(connectionId)).add(msg);
			} else {
				this.messagesMap.get(this.getUserName(connectionId)).add(msg);
			}
		}
	}

	public String getUserName(int connectionId) {
		synchronized (this.isOnline) {
			return this.isOnline.get(connectionId);
		}
	}

	public List<String> getListOfFollowing(String name) {
		if (this.followersMap.get(name) == null)
			return new ArrayList<String>();
		return this.followersMap.get(name);
	}

	public List<String> getListOfFollowers(String name) {
		if (this.whoIsFollowingMe.get(name) == null)
			return new ArrayList<String>();
		return this.whoIsFollowingMe.get(name);
	}

	public boolean checkPassword(String name, String password) {
		if (this.usersInfo.containsKey(name))
			if (this.usersInfo.get(name).equals(password))
				return true;
		return false;

	}

	public boolean checkIfExists(String name) {
		synchronized (this.usersInfo) {
			if (this.usersInfo.containsKey(name))
				return true;
			else
				return false;
		}
	}

	public boolean checkIfOnline(int connectionId) {
		synchronized (this.isOnline) {
			return this.isOnline.containsKey(connectionId);
		}
	}

	public void connectUser(String name, int connectionId) {
		synchronized (this.isOnline) {
			this.isOnline.put(connectionId, name);
		}
	}

	public void disconnectUser(int connectionId) {
		synchronized (this.isOnline) {
			if (this.isOnline.containsKey(connectionId))
				this.isOnline.remove(connectionId);
		}
	}

	public List<String> getUsersByDate() {
		synchronized (this.registeredUsersByDate) {
			return this.registeredUsersByDate;
		}
	}

	public int CheckIfOnline(String name) {
		synchronized (this.isOnline) {
			for (Map.Entry<Integer, String> o : this.isOnline.entrySet()) {
				if (o.getValue().equals(name))
					return o.getKey();
			}
			return -1;
		}

	}

}
