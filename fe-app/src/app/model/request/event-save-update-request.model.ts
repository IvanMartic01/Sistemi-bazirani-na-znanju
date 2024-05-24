
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

}
