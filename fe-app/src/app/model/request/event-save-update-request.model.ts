import {CreateSpecialOfferRequest} from "./create-special-offer-request.model";

export interface CreateUpdateEventRequest {
  name: string;
  startDateTime: Date;
  endDateTime: Date;
  price: number;
  totalSeats: number;
  shortDescription: string;
  detailedDescription: string;
  organizationPlan: string;
  type: string;
  countryId: string;
  specialOffer?: CreateSpecialOfferRequest;

}
