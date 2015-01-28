using PushSharp;
using PushSharp.Android;
using SnapTown.DAL;
using SnapTown.DTO;
using SnapTown.Models;
using SnapTown.Utils;
using SnapTown.WebService.Converters;
using System;
using System.Collections.Generic;
using System.Drawing;
using System.IO;
using System.Linq;
using System.Net;
using System.Net.Http;
using System.Net.Http.Headers;
using System.Web.Hosting;
using System.Web.Http;

namespace SnapTown.WebService.Controllers
{
    [RoutePrefix("api/media")]
    public class MediaController : ApiController
    {
        private IUnitOfWork unitOfWork { get; set; }
        public MediaController(IUnitOfWork unitOfWork)
        {
            this.unitOfWork = unitOfWork;
        }

        [Route("{townId:int}")]
        public IQueryable<MediaDto> GetAllByTown(int townId, string authToken)
        {
            return unitOfWork.Media.Filter(m => m.TownID == townId)
                .Select(MediaConverter.AsMediaDto);
        }

        public HttpResponseMessage Post(string authToken, int townId)
        {

            var result = new HttpResponseMessage(HttpStatusCode.OK);
            if (Request.Content.IsMimeMultipartContent())
            {
                var user = this.unitOfWork.Users.Get(x => x.AuthToken == authToken);
                var town = this.unitOfWork.Towns.Get(x => x.TownID == townId);


                Request.Content.ReadAsMultipartAsync<MultipartMemoryStreamProvider>(
                    new MultipartMemoryStreamProvider()).ContinueWith((task) =>
                {
                    MultipartMemoryStreamProvider provider = task.Result;
                    foreach (HttpContent content in provider.Contents)
                    {
                        Stream stream = content.ReadAsStreamAsync().Result;
                        Image image = Image.FromStream(stream);
                        var mimeType = content.Headers.ContentType.MediaType;

                        if (mimeType != "image/jpeg" && mimeType != "video/mp4")
                        {
                            throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotAcceptable,
                   "The file is not acceptable"));
                        }

                        var media = new Media(mimeType)
                        {
                            OwnerID = user.UserID,
                            TownID = town.TownID,
                            UploadedOn = DateTime.Now
                        };
                        this.unitOfWork.Media.Create(media);
                        this.unitOfWork.SaveChanges();

                        try
                        {

                            String filePath = HostingEnvironment.MapPath(String.Format("~/{0}/",
                                Constants.ImageFolder));

                            String folder = Path.Combine(filePath, media.TownID.ToString());
                            Directory.CreateDirectory(folder);

                            var fullPath = Path.Combine(filePath, media.Path);
                            image.Save(fullPath);

                            notifyAllSubscribers(town);
                        }
                        catch (Exception e)
                        {
                            this.unitOfWork.Media.Delete(media);
                            this.unitOfWork.SaveChanges();

                            throw;
                        }
                    }
                });

                return result;
            }
            else
            {
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotAcceptable,
                    "This request is not properly formatted."));
            }
        }

        [Route("{mediaId:int}")]
        public HttpResponseMessage Get(int mediaId)
        {

            var media = this.unitOfWork.Media.Get(x => x.MediaID == mediaId);

            if (media == null)
            {
                throw new HttpResponseException(Request.CreateResponse(HttpStatusCode.NotFound,
                                   "This media was not found."));
            }

            var serverPath = HostingEnvironment.MapPath(String.Format("~/{0}/",
                                Constants.ImageFolder));

            var fullPath = Path.Combine(serverPath, media.Path);
            HttpResponseMessage result = new HttpResponseMessage(HttpStatusCode.OK);
            var stream = new FileStream(fullPath, FileMode.Open);
            result.Content = new StreamContent(stream);

            switch (media.Type)
            {
                case MediaType.Photo:
                    result.Content.Headers.ContentType = new MediaTypeHeaderValue("image/jpeg");
                    break;
                case MediaType.Video:
                    result.Content.Headers.ContentType = new MediaTypeHeaderValue("video/mp4");
                    break;
            }
            return result;
        }

        private void notifyAllSubscribers(Town town)
        {

            var registrationIds = this.unitOfWork.Subscriptions
                .Filter(s => s.TownID == town.TownID, new string[] { "User" })
                .Select(s => s.User.GCMClientToken);
            //Create our push services broker
            var push = new PushBroker();

            push.RegisterGcmService(new GcmPushChannelSettings("AIzaSyDZwSkJL1KowOSLWD9dgbJaY1qsqMTLsc8"));

            push.QueueNotification(new GcmNotification().ForDeviceRegistrationId(registrationIds)
                                  .WithJson(@"{""alert"":""Hello World!"",""badge"":7,""sound"":""sound.caf""}"));

            //Stop and wait for the queues to drains
            push.StopAllServices();
        }
    }
}
