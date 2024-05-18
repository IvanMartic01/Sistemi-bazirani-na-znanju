import {CreateUser} from "./create-user.model";

export interface CreateVisitor extends CreateUser {

  countryId?: string;
  preferences?: Array<string>

}
