import { CreateAction } from "../../utils/CreateAction";
import { USER_ACTION_TYPE } from "./user.type";
export const setCurrentUser = (user) => {
  return CreateAction(USER_ACTION_TYPE.SET_CURRENT_USER, user);
};

export const removeCurrentUser = (user) => {
  return CreateAction(USER_ACTION_TYPE.REMOVE_CURRENT_USER, user);
};

export const updateCurrentUser = (...update) => {
  return CreateAction(USER_ACTION_TYPE.UPDATE_CURRENT_USER_PHOTO, update);
};
export const updateCurrentUserToken = (token) => {
  return CreateAction(USER_ACTION_TYPE.UPDATE_CURRENT_SUER_TOKEN, token);
};
