import {OrganizerResponse} from "../../../model/user/organizer-response.model";

export interface EventResponse {

  id: string;
  name: string;
  startDateTime: string;
  endDateTime: string;
  seats: number;
  price: number;
  shortDescription: string;
  detailedDescription: string;
  organizationPlan: string;
  organizer: OrganizerResponse;
  numberOfAvailableSeats: number;
}
